-- Скрипт для детального дебага (диффа) путей между Camunda и Новым движком для конкретных пар (run_id, instanceId)

WITH
-- 0. Массив проблемных проходок (вставьте сюда скопированный массив из stats скрипта)
target_pairs AS (
    SELECT tuple(run_id, instance_id) AS pair FROM VALUES(
        'run_id UInt64, instance_id String',
        (22517998173619295, '57f302d0-fe71-4544-b94d-c979bca25d0f'),
        (27021597764771314, 'abb9a4cc-50af-4771-b34d-bba26014a0a7'),
        (9007199255294116, '9848917f-5c68-4b9b-809c-1a461af27c11'),
        (22517998140381447, 'a6596785-e5a4-4cbb-b0f7-ff45380c3ea4'),
        (2251799855536077, '43a7007e-3494-4310-930c-fee4459bc9c9'),
        (13510798882632742, 'fddfea8f-d4f2-4224-ae72-be6b84ae8620'),
        (13510798923029876, '45607db7-9fed-4485-a43a-0d615fc9632a'),
        (20266198326132827, 'e6982168-f602-451b-8efb-09e33e7e8546'),
        (15762598696348243, 'c2143b48-d1b2-4253-9c69-1fc2e76af1a1'),
        (6755399444602945, '81966473-8a15-4704-a55f-32907063cbaf'),
        (18014398515494053, '54b44b3d-ca72-4dbb-8cf1-b4e1333acec1'),
        (13510798885640859, '19b87cc9-7e6d-4a5b-b103-177b755507d2'),
        (11258999068966537, '5282bf33-692d-4b79-a8fe-695a9d0e0157'),
        (27021597767759564, '153c66a3-859a-4853-a26b-baee282835d1'),
        (9007199258262576, 'd29d15b9-8fc4-4234-b29b-4cbf1454d3d3'),
        (2251799816589319, '900a808f-5bde-485c-911b-936e38b9aa32'),
        (2251799817222796, '19372f23-7f3d-4be3-9c27-5b96d9eb603c'),
        (15762598698716014, '83e8c45b-0ffb-496f-bff5-d32dddb9ce78'),
        (24769797953461863, '0bf93464-6d64-4960-b755-58afcd4b94a6'),
        (13510798888146849, '2178c2fc-c017-405c-b510-fe590eee7677'),
        (13510798922694979, 'c72470e0-1198-4be4-8c3b-a87aae5c80f4'),
        (15762598701794150, 'c1141867-d60c-4aab-ba6d-b9e4ffda654e'),
        (15762598699342812, '2de2f76c-8fb4-4320-96c5-4e5cab20e6ca'),
        (22517998140372971, '524734eb-e1ae-42ed-9306-462e5fd89e88'),
        (13510798885642539, '9a482476-53df-4744-905e-f2dc33cac93f'),
        (4503599630906580, '6530a84c-6ffd-423e-97b3-35cb935a39a8'),
        (22517998140371676, 'ccc5a818-4e54-46af-98ec-0c91bebc14ea'),
        (22517998142839477, '2d1ffacf-8059-4192-9e3f-fef1ff572ea9'),
        (13510798888119195, 'b0d84a30-3a9c-444e-ac42-d4af106a4541'),
        (22517998140376614, '042f4059-1d82-48dc-8624-d9297c404106'),
        (13510798922701360, '117fb4f4-7953-45e0-91b0-0226200a3b8b'),
        (22517998140379985, 'd5a8938d-6a88-400e-bc34-6f890d49ba21'),
        (18014398513018248, '3945f2fd-50e0-43ce-998e-354ceea08973'),
        (2251799819568749, '153d3fa2-e120-474b-b499-c583c4a8d19c'),
        (18014398513013825, '2f7ff762-edfb-4731-8f98-031ad64a28c4'),
        (18014398513018955, '02ac5e55-5b34-4f5c-bdbf-e1bdd2b34275'),
        (11258999071344180, 'c01ab63b-cd5a-4613-a8e4-4a6551bebab2'),
        (6755399443964584, '66ae2fba-29ba-4fef-8cd9-3ecf29584e8e'),
        (6755399441590498, '45bc6547-b3c8-4dd1-ba0b-42c09e872e42'),
        (13510798924919348, '1961a771-adc8-4015-8bfb-cf0a4cd9abdd'),
        (2251799817220682, 'f2b01e6d-ffd2-4af2-b475-90419c9e63cd'),
        (6755399444592858, '3a8d6a80-386c-468f-bb22-8042cf4b4acb'),
        (18014398509910384, 'edf81e25-a99e-4ec5-a734-0ab3a81a281b'),
        (18014398509966405, 'd7b7b49c-be78-482b-b0ff-b528a49e3f18'),
        (11258999105494606, '72a2beed-4787-4b49-bbbe-17c0a813e985'),
        (13510798922741050, 'fc1571a9-5458-4aaf-b746-667c75b68faf'),
        (24769797952832005, '58cd78aa-bd7a-45ce-989f-630b0edcc487'),
        (6755399444594797, 'c1ee2934-65ee-47e9-b9a0-620ab5171a3d'),
        (13510798923018805, 'a884b754-e41a-46f8-bbe7-c3c10dc2dcfa'),
        (20266198323697972, '9b9494f9-18d0-48fd-9daa-ae8a79da23f7'),
        (22517998137275686, 'dac07534-21bf-4cf6-91ba-87d49be27481'),
        (20266198326110338, 'f1855423-8010-430b-bb03-a912be5c5480'),
        (13510798882680321, 'be684a94-65b1-4e42-8f10-0efcabfd80fe'),
        (18014398510044257, 'd7e9ef77-9c8f-4477-be48-48220c9cb7aa'),
        (13510798923041425, '2309f55b-d044-4e2d-b4d6-ca83f920cd1f'),
        (4503599633378172, 'df4ff1a2-7fbe-40d2-a641-83680ba97583'),
        (15762598696343282, '85520306-1c2a-4066-8c10-13affec8d06b'),
        (15762598732687553, '46432b35-7697-42d7-8816-b0cefa74549b'),
        (9007199260721000, 'c1ef5601-28b4-485c-895d-04b4f17e9a8f'),
        (27021597767172119, '25ad5e1e-b7fc-4832-9286-34f25a537f82')
    )
),

-- 1. Сбор успешных проходок из Camunda
camunda_executions AS (
    SELECT
        zs.event_id AS event_id,
        zs.rule_name AS rule_name,
        zs.run_id AS run_id,
        -- Собираем массив элементов, отсеивая имя правила
        arrayFilter(x -> x != zs.rule_name, groupArray(ze.element_id)) AS camunda_path
    FROM reporting.zeebe_rules_started zs
    JOIN (
        SELECT run_id, element_id
        FROM reporting.zeebe_rules_elements
        ORDER BY run_id, position ASC
    ) ze ON zs.run_id = ze.run_id
    WHERE zs.run_id NOT IN (SELECT run_id FROM reporting.zeebe_erroneous_rules_ends)
    GROUP BY zs.event_id, zs.rule_name, zs.run_id
),

-- 2. Сбор успешных проходок из Нового Engine
custom_engine_executions AS (
    SELECT
        JSONExtractString(variables, 'event', 'id') AS event_id,
        ruleName AS rule_name,
        instanceId AS instance_id,
        -- Собираем массив, отсеивая имя правила
        arrayFilter(x -> x != ruleName, arrayConcat([argMin(fromId, ts)], groupArray(toId))) AS custom_path
    FROM (
        SELECT *
        FROM reporting.tre___audit_raw
        WHERE nodePhase = 'transition'
        ORDER BY instanceId, startedAt ASC, ts ASC
    )
    GROUP BY event_id, rule_name, instance_id
    HAVING max(failed) = 0
)

-- 3. Сравнение путей и вывод диффа
SELECT
    c.event_id,
    c.rule_name,
    c.run_id AS camunda_run_id,
    ce.instance_id AS custom_instance_id,
    c.camunda_path,
    ce.custom_path,
    -- Элементы, которые есть в Camunda, но нет в Custom Engine
    arrayFilter(x -> NOT has(ce.custom_path, x), c.camunda_path) AS elements_only_in_camunda,
    -- Элементы, которые есть в Custom Engine, но нет в Camunda
    arrayFilter(x -> NOT has(c.camunda_path, x), ce.custom_path) AS elements_only_in_custom
FROM camunda_executions c
JOIN custom_engine_executions ce
  ON c.event_id = ce.event_id AND c.rule_name = ce.rule_name
WHERE tuple(c.run_id, ce.instance_id) IN target_pairs;