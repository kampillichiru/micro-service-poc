import java.util.List;

public class TypeSafeCaster {
    @SuppressWarnings("unchecked")
    public static <T> List<T> castToList(Object obj, Class<T> clazz) {
        if (obj instanceof List<?>) {
            for (Object element : (List<?>) obj) {
                if (element != null && !clazz.isInstance(element)) {
                    throw new ClassCastException("Cannot cast element of type " +
                        element.getClass().getName() + " to " + clazz.getName());
                }
            }
            return (List<T>) obj;
        }
        throw new ClassCastException("Cannot cast object of type " +
            obj.getClass().getName() + " to List<" + clazz.getName() + ">");
    }
}
