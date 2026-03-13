-- ClickHouse SQL query to compare execution paths of Camunda (Zeebe) and the Custom Engine.

WITH
-- 1. Сбор успешных проходок из Camunda
camunda_executions AS (
    SELECT
        zs.event_id AS event_id,
        zs.rule_name AS rule_name,
        zs.run_id AS run_id,
        -- Собираем массив элементов, динамически отфильтровывая элемент, который равен rule_name (псевдо-узел завершения процесса)
        arrayFilter(x -> x != zs.rule_name, groupArray(ze.element_id)) AS path
    FROM reporting.zeebe_rules_started zs
    JOIN (
        SELECT run_id, element_id
        FROM reporting.zeebe_rules_elements
        ORDER BY run_id, position ASC
    ) ze ON zs.run_id = ze.run_id
    -- Исключаем проходки с ошибками надежным способом
    WHERE zs.run_id NOT IN (SELECT run_id FROM reporting.zeebe_erroneous_rules_ends)
    GROUP BY zs.event_id, zs.rule_name, zs.run_id
),

-- 2. Сбор успешных проходок из Нового Engine
custom_engine_executions AS (
    SELECT
        JSONExtractString(variables, 'event', 'id') AS event_id,
        ruleName AS rule_name,
        instanceId AS instance_id,
        -- Восстанавливаем цепочку переходов (fromId -> toId)
        arrayConcat([any(fromId)], groupArray(toId)) AS path
    FROM (
        SELECT *
        FROM reporting.tre___audit_raw
        WHERE nodePhase = 'transition'
        ORDER BY instanceId, startedAt ASC, ts ASC
    )
    GROUP BY event_id, rule_name, instance_id
    -- Исключаем инстансы, в которых была зафиксирована ошибка
    HAVING max(failed) = 0
),

-- 3. Сравнение путей по event_id и rule_name
comparison AS (
    SELECT
        c.event_id,
        c.rule_name,
        c.run_id AS camunda_run_id,
        ce.instance_id AS custom_instance_id,
        c.path AS camunda_path,
        ce.path AS custom_path,
        -- Пути считаются совпавшими, если массивы идентичны
        (c.path = ce.path) AS is_match
    FROM camunda_executions c
    JOIN custom_engine_executions ce
      ON c.event_id = ce.event_id AND c.rule_name = ce.rule_name
)

-- 4. Вывод итоговой статистики
SELECT
    rule_name,
    count(*) AS total_successful_runs,
    sum(is_match) AS matching_runs,
    round((sum(is_match) / count(*)) * 100, 2) AS match_percentage,
    -- Если пути не совпали, выводим кортеж (Camunda run_id, Custom instance_id)
    groupArrayIf(tuple(camunda_run_id, custom_instance_id), is_match = 0) AS mismatched_instances
FROM comparison
GROUP BY rule_name
ORDER BY match_percentage DESC;
