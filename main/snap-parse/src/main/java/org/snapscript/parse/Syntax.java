package org.snapscript.parse;

public enum Syntax {
   SIGN("sign", "{'-'|'+'}"),
   NUMBER("number", "?<sign>{[hexidecimal]|[binary]|[decimal]}"), 
   BOOLEAN("boolean", "{'true'|'false'}"),
   NULL("null", "'null'"),
   LITERAL("literal", "{<null>|<boolean>|<number>|[template]|[text]}"),
   ASSIGNMENT_OPERATOR("assignment-operator", "{'='|'*='|'/='|'%='|'+='|'-='|'<<='|'>>='|'>>>='|'&='|'^='|'|='}"),
   ARITHMETIC_OPERATOR("arithmetic-operator", "{'+'|'-'|'*'|'/'|'%'}"),
   BINARY_OPERATOR("binary-operator", "{'&'|'|'|'^'|'>>>'|'>>'|'<<'}"),
   COMPARISON_OPERATOR("comparison-operator", "{'>='|'<='|'>'|'<'|'=='|'!='}"),
   CONDITIONAL_OPERATOR("conditional-operator", "{'&&'|'||'}"),
   PREFIX_OPERATOR("prefix-operator", "{'!'|'~'|'+'|'-'}"),
   INCREMENT_OPERATOR("increment-operator", "'++'"),
   DECREMENT_OPERATOR("decrement-operator", "'--'"),
   THIS("this", "'this'"),
   CLASS("class", "'class'"),
   VARIABLE("variable", "{<this>|<class>|[identifier]|[type]}"),          // ((array[x])[x] -> (<var>[])
   VARIABLE_REFERENCE("variable-reference", "<variable>"),
   RANGE("range", "<number>'..'<number>"),
   ARRAY("array", "{<function-invocation>|<variable-reference>}"),
   ARRAY_INDEX("array-index", "<array>+('['<argument>']')"),   
   FUNCTION("function", "[identifier]"),
   FUNCTION_INVOCATION("function-invocation", "<function>'('?<argument-list>')'"),
   REFERENCE("reference", "<reference-list>"),
   REFERENCE_PART("reference-part", "{<array-index>|<function-invocation>|<construct>|<closure>|<variable-reference>|<literal>}"),
   REFERENCE_LIST("reference-list", "<reference-part>*('.'<reference-list>)"),
   ARRAY_ENTRY("array-entry", "<argument>"),
   ARRAY_ENTRY_LIST("array-entry-list", "<array-entry>*(','<array-entry>)"),
   SET_ENTRY("set-entry", "<argument>"),
   SET_ENTRY_LIST("set-entry-list", "<set-entry>*(','<set-entry>)"),
   MAP_ENTRY("map-entry", "([identifier]|<literal>)':'<argument>"),
   MAP_ENTRY_LIST("map-entry-list", "<map-entry>*(','<map-entry>)"),
   CONSTRUCT_TYPE("construct-type", "[type]"),
   CONSTRUCT_OBJECT("construct-object", "'new'' '<construct-type>'('?<argument-list>')'"),
   CONSTRUCT_LIST("construct-list", "'[]'|'['+<array-entry-list>']'"),
   CONSTRUCT_ARRAY("construct-array", "<construct-type>{'[]'|'['+<array-entry-list>']'}"),
   CONSTRUCT_OBJECT_ARRAY("construct-object-array", "'new'' '<construct-type>+('['<argument>']')"),
   CONSTRUCT_SET("construct-set", "'{}'|('{'+<set-entry-list>'}')"),
   CONSTRUCT_MAP("construct-map", "'{:}'|('{'+<map-entry-list>'}')"),
   CONSTRUCT("construct", "{<construct-object>|<construct-object-array>|<construct-list>|<construct-array>|<construct-map>|<construct-set>}"),
   ARGUMENT("argument", "{<literal>|<increment-decrement-operand>|<prefix-operand>|<reference>}|<choice>|<conditional>|<calculation>"),
   ARGUMENT_LIST("argument-list", "<argument>*(','<argument>)"),
   ASSIGNMENT("assignment", "<reference>?' '<assignment-operator>?' '<expression>"),
   ASSIGNMENT_OPERAND("assignment-operand", "'('<assignment>')'"),
   PREFIX_OPERATION("prefix-operation", "<prefix-operator><reference>"),
   PREFIX_OPERAND("prefix-operand", "{<prefix-operation>|'('<prefix-operation>')'}"),
   INCREMENT("increment", "{<postfix-increment>|<prefix-increment>}"),
   PREFIX_INCREMENT("prefix-increment", "<increment-operator><reference>"),
   POSTFIX_INCREMENT("postfix-increment", "<reference><increment-operator>"),
   DECREMENT("decrement", "{<postfix-decrement>|<prefix-decrement>}"),
   PREFIX_DECREMENT("prefix-decrement", "<decrement-operator><reference>"),
   POSTFIX_DECREMENT("postfix-decrement", "<reference><decrement-operator>"),
   INCREMENT_DECREMENT("increment-decrement", "<increment>|<decrement>"),
   INCREMENT_DECREMENT_OPERAND("increment-decrement-operand", "<increment-decrement>|'('<increment-decrement>')'"),
   REFERENCE_OPERAND("reference-operand", "<reference>|'('<reference>')'"),
   VALUE_OPERAND("value-operand", "<increment-decrement-operand>|<literal>|<prefix-operand>|<reference-operand>"),
   COMPARISON_OPERAND("comparison-operand", "<value-operand>|<calculation>|<assignment-operand>"),
   COMPARISON("comparison", "<comparison-operand>?' '<comparison-operator>?' '<comparison-operand>|'('<comparison>')'"),
   CONDITIONAL_OPERAND("conditional-operand", "<comparison>|<value-operand>|<assignment-operand>|<boolean>|'('<conditional-operand>')'|'('<conditional-list>')'"),
   CONDITIONAL_LIST("conditional-list", "<conditional-operand>*(?' '<conditional-operator>?' '<conditional-operand>)"),
   CONDITIONAL("conditional", "<conditional-list>|'('<conditional>')'"),
   CHOICE("choice", "<conditional>'?'<expression>':'<expression>"),   
   CALCULATION_OPERATOR("calculation-operator", "<arithmetic-operator>|<binary-operator>"),
   CALCULATION_OPERAND("calculation-operand", "<assignment-operand>|<value-operand>|'('<calculation-operand>')'|'('<calculation-list>')'"),
   CALCULATION_LIST("calculation-list", "<calculation-operand>?' '<calculation-operator>?' '<calculation-operand>*(<calculation-operator><calculation-operand>)"),
   CALCULATION("calculation", "<calculation-list>|'('<calculation>')'"),
   EXPRESSION("expression", "{<literal>|<increment-decrement-operand>|<reference>|<prefix-operation>}|<assignment>|<calculation>|<choice>|<comparison>|<conditional>|'('<expression>')'"),
   TYPE_CONSTRAINT("type-constraint", "[type]"),
   RETURN_STATEMENT("return-statement", "'return'*(?' '<expression>)';'"),
   BREAK_STATEMENT("break-statement", "'break;'"),
   CONTINUE_STATEMENT("continue-statement", "'continue;'"),
   THROW_STATEMENT("throw-statement", "'throw'?' '(<reference>|<literal>)';'"),
   EXPRESSION_STATEMENT("expression-statement", "(<reference>|<assignment>|<increment-decrement>)';'"),
   COMPOUND_STATEMENT("compound-statement", "'{'+<statement>'}'"),
   TERMINAL_STATEMENT("terminal-statement", "';'"),
   EMPTY_STATEMENT("empty-statement", "'{}'"),
   GROUP_STATEMENT("group-statement", "{<compound-statement>|<empty-statement>}"),
   CONTROL_STATEMENT("control-statement", "{<return-statement>|<throw-statement>|<break-statement>|<continue-statement>}"),
   STATEMENT("statement", "{<control-statement>|<try-statement>|<expression-statement>|<conditional-statement>|<declaration-statement>|<group-statement>|<terminal-statement>}"),
   ASSIGNMENT_VARIABLE("assignment-variable", "[identifier]"),
   ASSIGNMENT_EXPRESSION("assignment-expression", "(<literal>|<reference>|<calculation>|<choice>|<conditional>)"),
   ASSIGNMENT_STATEMENT("assignment-statement", "<assignment-variable>?('='<assignment-expression>)';'"),     
   DECLARE_VARIABLE("declare-variable", "'var'' '<assignment-variable>?(':'<type-constraint>)?('='<assignment-expression>)"),
   DECLARE_CONSTANT("declare-constant", "'const'' '<assignment-variable>?(':'<type-constraint>)?('='<assignment-expression>)"),
   DECLARATION_STATEMENT("declaration-statement", "{<declare-variable>|<declare-constant>}';'"),   
   CONDITIONAL_STATEMENT("conditional-statement", "{<if-statement>|<while-statement>|<for-statement>|<for-in-statement>|<loop-statement>}"),
   IF_STATEMENT("if-statement", "'if('<conditional>')'<statement>?('else'?' '<statement>)"),  
   WHILE_STATEMENT("while-statement", "'while('<conditional>')'<statement>"),
   FOR_STATEMENT("for-statement", "'for('(<declaration-statement>|<assignment-statement>|<terminal-statement>)<conditional>';'?(<assignment>|<increment-decrement>)')'<statement>"),
   FOR_IN_STATEMENT("for-in-statement", "'for('?('var'' ')([identifier])' ''in'?' '{<range>|<reference>}')'<statement>"),
   LOOP_STATEMENT("loop-statement", "{'loop'|'for(;;)'}<statement>"),
   TRY_BODY("try-body", "<group-statement>"),
   CATCH_BODY("catch-body", "<group-statement>"),
   FINALLY_BODY("finally-body", "<group-statement>"),
   TRY_CATCH_STATEMENT("try-catch-statement", "'try'<try-body>'catch('<parameter-declaration>')'<catch-body>?('finally'<finally-body>)"),
   TRY_FINALLY_STATEMENT("try-finally-statement", "'try'<try-body>'finally'<finally-body>)"),
   TRY_STATEMENT("try-statement", "{<try-catch-statement>|<try-finally-statement>}"),
   VARIABLE_ARGUMENT("variable-argument", "'...'"),
   PARAMETER_NAME("parameter-name", "[identifier]"),
   PARAMETER_DECLARATION("parameter-declaration", "<parameter-name>?<variable-argument>?(':'<type-constraint>)"),
   PARAMETER_LIST("parameter-list", "?(<parameter-declaration>*(','<parameter-declaration>))"),
   CLOSURE_PARAMETER_LIST("closure-parameter-list", "'('<parameter-list>')'|?<parameter-declaration>"),
   CLOSURE("closure", "<closure-parameter-list>?' ''->'?' '{<group-statement>|<expression>}"),
   TYPE_NAME("type-name", "[type]"),
   TRAIT_NAME("trait-name", "[type]"),
   MODIFIER("modifier", "{'var'|'const'|'static'|'public'|'private'|'function'|'abstract'|'override'}"),
   MODIFIER_LIST("modifier-list", "*(<modifier>' ')"),         
   MEMBER_FIELD("member-field", "<modifier-list><assignment-variable>?(':'<type-constraint>)?('='<assignment-expression>)';'"),
   MEMBER_FUNCTION("member-function", "<modifier-list><function>'('<parameter-list>')'<group-statement>"),   
   MEMBER_CONSTRUCTOR("member-constructor", "<modifier-list>'new('<parameter-list>')'?(':'{<this-constructor>|<super-constructor>})<group-statement>"),
   SUPER_CONSTRUCTOR("super-constructor", "'super('<argument-list>')'"),
   THIS_CONSTRUCTOR("this-constructor", "'this('<argument-list>')'"),
   CLASS_HIERARCHY("class-hierarchy", "?{' ''extends'' '<type-name>|' ''with'' '<trait-name>}*(' ''with'' '<trait-name>)"),
   CLASS_DEFINITION("class-definition", "'class'' '<type-name>?<class-hierarchy>{'{}'|'{'*(<member-field>|<member-constructor>|<member-function>)'}'}"),
   TRAIT_HIERARCHY("trait-hierarchy", "*(' ''extends'' '<trait-name>)"),
   TRAIT_FUNCTION("trait-function", "?<modifier-list><function>'('<parameter-list>')'{';'|<group-statement>}"),
   TRAIT_DEFINITION("trait-definition", "'trait'' '<trait-name>?<trait-hierarchy>{'{}'|'{'*<trait-function>'}'}"),   
   ENUM_HIERARCHY("enum-hierarchy", "*(' ''with'' '<trait-name>)}"),
   ENUM_KEY("enum-key", "[identifier]"),
   ENUM_VALUE("enum-value", "<enum-key>?('('<argument-list>')')"),
   ENUM_LIST("enum-list", "<enum-value>*(','<enum-value>)"),
   ENUM_CONSTRUCTOR("enum-constructor", "<modifier-list>'new('<parameter-list>')'<group-statement>"),
   ENUM_FIELD("enum-field", "<modifier-list><assignment-variable>?(':'<type-constraint>)?('='<assignment-expression>)';'"),
   ENUM_FUNCTION("enum-function", "<modifier-list><function>'('<parameter-list>')'<group-statement>"),   
   ENUM_DEFINITION("enum-definition", "'enum'' '<type-name>?<enum-hierarchy>'{'<enum-list>?(';'*(<enum-field>|<enum-constructor>|<enum-function>))'}'"),   
   TYPE_DEFINITION("type-definition", "{<class-definition>|<trait-definition>|<enum-definition>}"),
   FULL_QUALIFIER("full-qualifier", "[qualifier]*('.'[qualifier])"),
   WILD_QUALIFIER("wild-qualifier", "[qualifier]*('.'[qualifier])'.*'"),
   IMPORT_STATIC("import-static", "'import static'' '(<full-qualifier>|<wild-qualifier>)';'"),
   IMPORT("import", "'import'' '(<full-qualifier>|<wild-qualifier>)?(' as '[type])';'"),   
   MODULE_NAME("module-name", "[identifier]"),
   MODULE_FUNCTION("module-function", "'function'' '<function>'('<parameter-list>')'<group-statement>"),
   MODULE_STATEMENT("module-statement", "{<try-statement>|<declaration-statement>|<conditional-statement>|<type-definition>|<expression-statement>}"),   
   MODULE_DEFINITION("module-definition", "'module'' '<module-name>{<empty-statement>|'{'*{<module-function>|<module-statement>}'}'}"),   
   SCRIPT_IMPORT("script-import", "<import-static>|<import>"),
   SCRIPT_FUNCTION("script-function", "'function'' '<function>'('<parameter-list>')'<group-statement>"),
   SCRIPT_STATEMENT("script-statement", "{<try-statement>|<declaration-statement>|<conditional-statement>|<type-definition>|<module-definition>|<expression-statement>}"),
   SCRIPT_PACKAGE("script-package", "*<script-import>*{<type-definition>|<module-definition>}"),
   SCRIPT("script", "*<script-import>*{<script-function>|<script-statement>}");
   
   public final String name;
   public final String grammar;
   
   private Syntax(String name, String grammar) {
      this.name = name;
      this.grammar = grammar;
   }
   
   public String getName(){
      return name;
   }
   
   public String getGrammar() {
      return grammar;
   }
}
