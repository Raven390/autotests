package generator.app;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

import com.fasterxml.jackson.core.type.TypeReference;
import generator.annotations.RuleTestData;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.reflections.Reflections;

final class RuleTypeOperations {

    public record MethodInfo(Method method, TypeReference<?> returnTypeRef, TypeReference<?>[] paramTypeRefs) {}

    private final Map<String, Set<Class<?>>> annotatedClasses;

    public RuleTypeOperations(String packagePrefix) {
        annotatedClasses = new Reflections(packagePrefix)
                .getTypesAnnotatedWith(RuleTestData.class).stream()
                        .collect(groupingBy(
                                cl -> cl.getAnnotation(RuleTestData.class).value(), toSet()));
    }

    public Optional<MethodInfo> getMethodInfo(String ruleName, Predicate<? super Method> predicate) {
        return annotatedClasses.getOrDefault(ruleName, emptySet()).stream()
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
                .filter(predicate)
                .findFirst()
                .map(method -> {
                    var returnType = new TypeReference<>() {
                        @Override
                        public Type getType() {
                            return method.getGenericReturnType();
                        }
                    };
                    var methodParams = method.getGenericParameterTypes();
                    var paramTypes = new TypeReference<?>[methodParams.length];
                    var i = 0;
                    for (var p : methodParams) {
                        paramTypes[i++] = new TypeReference<>() {
                            @Override
                            public Type getType() {
                                return p;
                            }
                        };
                    }
                    return new MethodInfo(method, returnType, paramTypes);
                });
    }

    public Collection<String> getAllRuleNames() {
        return annotatedClasses.keySet();
    }
}
