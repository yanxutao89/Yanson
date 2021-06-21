package yanson.json;

/**
 * @Author: Yanxt7
 * @Desc:
 * @Date: 2020/12/20 17:27
 */
public class Constants {

    public static final String MAGIC = "luxkui";
    public static final String DOUBLE_QUOTATIONS = "\"";

    public static final String[] SUPPORTED_VALUE_TYPES = { "string", "number", "boolean", "null", "object", "array"};

    /**
     * name-separator   = ws %x3A ws ; : colon
     * value-separator  = ws %x2C ws ; , comma
     * begin-object     = ws %x7B ws ; { left curly bracket
     * end-object       = ws %x7D ws ; } right curly bracket
     * begin-array      = ws %x5B ws ; [ left square bracket
     * end-array        = ws %x5D ws ; ] right square bracket
     * Insignificant whitespace is allowed before or after any of the six
     * structural characters.
     * ws = *(
     *      %x20 / ; Space
     *      %x09 / ; Horizontal tab
     *      %x0A / ; Line feed or New line
     *      %x0D ; Carriage return
     *       )
     */
    public static final String COLON = ":";
    public static final String COMMA = ",";
    public static final String LEFT_CURLY_BRACKET = "{";
    public static final String RIGHT_CURLY_BRACKET = "}";
    public static final String LEFT_SQUARE_BRACKET = "[";
    public static final String RIGHT_SQUARE_BRACKET = "]";
    public static final String ARRAY_VALUE_WITH_PRIMITIVE_TYPES = "IsArrayEmptyOrNonObjectSeparatedByComma";

}
