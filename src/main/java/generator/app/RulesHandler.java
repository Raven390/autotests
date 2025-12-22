package generator.app;

import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class RulesHandler {

    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final RuleTypeOperations typeOperations;
    private final MetadataRepository metadataRepository;

    public RulesHandler(RuleTypeOperations typeOperations, MetadataRepository metadataRepository) {
        this.typeOperations = typeOperations;
        this.metadataRepository = metadataRepository;
    }

    public Optional<RuleTypeOperations.MethodInfo> create(String name) throws IllegalStateException {
        var setupInfo = typeOperations.getMethodInfo(
                name,
                m -> isStatic(m.getModifiers())
                        && m.getParameterCount() == 0
                        && m.getName().startsWith("setup"));
        setupInfo.ifPresent(info -> executorService.execute(() -> {
            try {
                var returnValue = info.method().invoke(null);
                metadataRepository.save(name, returnValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        }));
        return setupInfo;
    }

    public Optional<RuleTypeOperations.MethodInfo> delete(String name) throws IllegalStateException {
        var deleteInfo = typeOperations.getMethodInfo(
                name,
                m -> isStatic(m.getModifiers())
                        && m.getParameterCount() == 1
                        && m.getName().startsWith("delete"));
        deleteInfo.ifPresent(info -> executorService.execute(
                () -> metadataRepository.find(name, info.paramTypeRefs()[0]).ifPresent(rule -> {
                    try {
                        info.method().invoke(null, rule.value());
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    }
                    metadataRepository.remove(name, rule.timestamp());
                })));
        return deleteInfo;
    }

    public Collection<String> getAll() {
        return typeOperations.getAllRuleNames();
    }

    public Optional<Object> getOne(String name) {
        return metadataRepository.find(name).map(MetadataRepository.Result::value);
    }
}
