package org.snapscript.compile.instruction;

import org.snapscript.compile.instruction.collection.Array;
import org.snapscript.compile.instruction.collection.ArrayIndex;
import org.snapscript.compile.instruction.collection.Range;
import org.snapscript.compile.instruction.condition.Choice;
import org.snapscript.compile.instruction.condition.Comparison;
import org.snapscript.compile.instruction.condition.ConditionalList;
import org.snapscript.compile.instruction.condition.ConditionalOperand;
import org.snapscript.compile.instruction.condition.ConditionalOperator;
import org.snapscript.compile.instruction.condition.ForInStatement;
import org.snapscript.compile.instruction.condition.ForInfiniteStatement;
import org.snapscript.compile.instruction.condition.ForStatement;
import org.snapscript.compile.instruction.condition.IfStatement;
import org.snapscript.compile.instruction.condition.WhileStatement;
import org.snapscript.compile.instruction.construct.ConstructArray;
import org.snapscript.compile.instruction.construct.ConstructList;
import org.snapscript.compile.instruction.construct.ConstructMap;
import org.snapscript.compile.instruction.construct.ConstructObject;
import org.snapscript.compile.instruction.construct.ConstructObjectArray;
import org.snapscript.compile.instruction.construct.ConstructSet;
import org.snapscript.compile.instruction.construct.MapEntry;
import org.snapscript.compile.instruction.construct.MapEntryList;
import org.snapscript.compile.instruction.define.ClassDefinition;
import org.snapscript.compile.instruction.define.EnumConstructor;
import org.snapscript.compile.instruction.define.EnumDefinition;
import org.snapscript.compile.instruction.define.EnumKey;
import org.snapscript.compile.instruction.define.EnumList;
import org.snapscript.compile.instruction.define.EnumValue;
import org.snapscript.compile.instruction.define.MemberConstructor;
import org.snapscript.compile.instruction.define.MemberField;
import org.snapscript.compile.instruction.define.MemberFunction;
import org.snapscript.compile.instruction.define.Modifier;
import org.snapscript.compile.instruction.define.ModifierList;
import org.snapscript.compile.instruction.define.ModuleDefinition;
import org.snapscript.compile.instruction.define.ModuleName;
import org.snapscript.compile.instruction.define.SuperConstructor;
import org.snapscript.compile.instruction.define.ThisConstructor;
import org.snapscript.compile.instruction.define.TraitDefinition;
import org.snapscript.compile.instruction.define.TraitFunction;
import org.snapscript.compile.instruction.define.TraitName;
import org.snapscript.compile.instruction.define.TypeHierarchy;
import org.snapscript.compile.instruction.define.TypeName;
import org.snapscript.compile.instruction.literal.BooleanLiteral;
import org.snapscript.compile.instruction.literal.NullLiteral;
import org.snapscript.compile.instruction.literal.NumberLiteral;
import org.snapscript.compile.instruction.literal.TextLiteral;
import org.snapscript.compile.instruction.operation.Assignment;
import org.snapscript.compile.instruction.operation.AssignmentStatement;
import org.snapscript.compile.instruction.operation.CalculationList;
import org.snapscript.compile.instruction.operation.CalculationOperand;
import org.snapscript.compile.instruction.operation.CalculationOperator;
import org.snapscript.compile.instruction.operation.PostfixDecrement;
import org.snapscript.compile.instruction.operation.PostfixIncrement;
import org.snapscript.compile.instruction.operation.PrefixDecrement;
import org.snapscript.compile.instruction.operation.PrefixIncrement;
import org.snapscript.compile.instruction.operation.PrefixOperation;
import org.snapscript.compile.instruction.operation.SignedNumber;
import org.snapscript.compile.instruction.template.TextTemplate;
import org.snapscript.compile.instruction.variable.Variable;

public enum Instruction {
   DECIMAL(NumberLiteral.class, "decimal", false),
   HEXIDECIMAL(NumberLiteral.class, "hexidecimal", false),    
   BOOLEAN(BooleanLiteral.class, "boolean", false),
   IDENTIFIER(TextLiteral.class, "identifier", false), 
   TEMPLATE(TextTemplate.class, "template", false),
   CLASS(TextLiteral.class, "class", false),  
   TYPE(TextLiteral.class, "type", false),   
   TEXT(TextLiteral.class, "text", false),
   THIS(TextLiteral.class, "this", false),
   NULL(NullLiteral.class, "null", false),
   NUMBER(SignedNumber.class, "number", false), 
   VARIABLE(Variable.class, "variable", false),
   ARGUMENT(Argument.class, "argument", false),
   RANGE(Range.class, "range", false),     
   ARRAY(Array.class, "array", false),      
   ARRAY_INDEX(ArrayIndex.class, "array-index", false),                
   FUNCTION_INVOCATION(FunctionInvocation.class, "function-invocation", true),           
   ARGUMENT_LIST(ArgumentList.class, "argument-list", false),     
   REFERENCE_LIST(ReferenceList.class, "reference-list", false),
   REFERENCE_PART(ReferencePart.class, "reference-part", false),
   CALCULATION_LIST(CalculationList.class, "calculation-list", false),
   CALCULATION_OPERATOR(CalculationOperator.class, "calculation-operator", false),
   CALCULATION_OPERAND(CalculationOperand.class, "calculation-operand", false),   
   COMPARISON(Comparison.class, "comparison", false),
   CONDITIONAL_LIST(ConditionalList.class, "conditional-list", false),
   CONDITIONAL_OPERATOR(ConditionalOperator.class, "conditional-operator", false),
   CONDITIONAL_OPERAND(ConditionalOperand.class, "conditional-operand", false),   
   POSTFIX_INCREMENT(PostfixIncrement.class, "postfix-increment", false),
   POSTFIX_DECREMENT(PostfixDecrement.class, "postfix-decrement", false),
   PREFIX_INCREMENT(PrefixIncrement.class, "prefix-increment", false),
   PREFIX_DECREMENT(PrefixDecrement.class, "prefix-decrement", false),
   PREFIX_OPERATION(PrefixOperation.class, "prefix-operation", false),   
   CHOICE(Choice.class, "choice", false), 
   ASSIGNMENT(Assignment.class, "assignment", false),
   CONSTRUCT_ARRAY(ConstructArray.class, "construct-array", true),
   CONSTRUCT_LIST(ConstructList.class, "construct-list", true),
   CONSTRUCT_OBJECT_ARRAY(ConstructObjectArray.class, "construct-object-array", true),
   CONSTRUCT_OBJECT(ConstructObject.class, "construct-object", true),
   CONSTRUCT_MAP(ConstructMap.class, "construct-map", true),   
   CONSTRUCT_SET(ConstructSet.class, "construct-set", true),
   ARRAY_ENRTY_LIST(ArgumentList.class, "array-entry-list", false),
   ARRAY_ENTRY(Argument.class, "array-entry", false),
   SET_ENRTY_LIST(ArgumentList.class, "set-entry-list", false),
   SET_ENTRY(Argument.class, "set-entry", false),
   MAP_ENRTY_LIST(MapEntryList.class, "map-entry-list", false),
   MAP_ENTRY(MapEntry.class, "map-entry", false),
   DECLARE_VARIABLE(DeclareVariable.class, "declare-variable", false),
   DECLARE_CONSTANT(DeclareConstant.class, "declare-constant", false),      
   DECLARATION_STATEMENT(DeclarationStatement.class, "declaration-statement", true),
   ASSIGNMENT_STATEMENT(AssignmentStatement.class, "assignment-statement", true),      
   EXPRESSION_STATEMENT(ExpressionStatement.class, "expression-statement", true),
   TERMINAL_STATEMENT(NoOperation.class, "terminal-statement", false),   
   BLANK_STATEMENT(NoOperation.class, "empty-statement", false),
   COMPOUND_STATEMENT(CompoundStatement.class, "compound-statement", false),   
   IF_STATEMENT(IfStatement.class, "if-statement", true),
   BREAK_STATEMENT(BreakStatement.class, "break-statement", true),
   CONTINUE_STATEMENT(ContinueStatement.class, "continue-statement", true),
   RETURN_STATEMENT(ReturnStatement.class, "return-statement", true),      
   WHILE_STATEMENT(WhileStatement.class, "while-statement", true),
   FOR_STATEMENT(ForStatement.class, "for-statement", true),
   FOR_INFINITE_STATEMENT(ForInfiniteStatement.class, "for-infinite-statement", true),
   FOR_IN_STATEMENT(ForInStatement.class, "for-in-statement", true),
   TYPE_CONSTRAINT(Constraint.class, "type-constraint", false),
   PARAMETER(Parameter.class, "parameter", false),
   PARAMETER_LIST(ParameterList.class, "parameter-list", false),
   THROW(ThrowStatement.class, "throw-statement", true),    
   TRY_CATCH(TryCatchStatement.class, "try-catch-statement", true),
   TRY_FINALLY(TryFinallyStatement.class, "try-finally-statement", true),      
   TYPE_NAME(TypeName.class, "type-name", false),
   TRAIT_NAME(TraitName.class, "trait-name", false),     
   TRAIT_HIERARCHY(TypeHierarchy.class, "trait-hierarchy", false),
   TRAIT_DEFINITION(TraitDefinition.class, "trait-definition", false),    
   TRAIT_FUNCTION(TraitFunction.class, "trait-function", false),
   ENUM_KEY(EnumKey.class, "enum-key", false),
   ENUM_HIERARCHY(TypeHierarchy.class, "enum-hierarchy", false),
   ENUM_FIELD(MemberField.class, "enum-field", false),
   ENUM_FUNCTION(MemberFunction.class, "enum-function", false),   
   ENUM_DEFINITION(EnumDefinition.class, "enum-definition", false), 
   ENUM_CONSTRUCTOR(EnumConstructor.class, "enum-constructor", false), 
   ENUM_VALUE(EnumValue.class, "enum-value", false),
   ENUM_LIST(EnumList.class, "enum-list", false),     
   CLASS_HIERARCHY(TypeHierarchy.class, "class-hierarchy", false),
   CLASS_DEFINITION(ClassDefinition.class, "class-definition", false),   
   MODIFIER(Modifier.class, "modifier", false),
   MODIFIER_LIST(ModifierList.class, "modifier-list", false), 
   MEMBER_FIELD(MemberField.class, "member-field", false),
   MEMBER_FUNCTION(MemberFunction.class, "member-function", false),   
   MEMBER_CONSTRUCTOR(MemberConstructor.class, "member-constructor", false),     
   SUPER_CONSTRUCTOR(SuperConstructor.class, "super-constructor", false),
   THIS_CONSTRUCTOR(ThisConstructor.class, "this-constructor", false),
   EXPRESSION(Expression.class, "expression", false),
   WILD_QUALIFIER(WildQualifier.class, "wild-qualifier", false),
   FULL_QUALIFIER(FullQualifier.class, "full-qualifier", false),
   IMPORT(Import.class, "import", false), 
   IMPORT_STATIC(ImportStatic.class, "import-static", false),
   MODULE_NAME(ModuleName.class, "module-name", false),    
   MODULE_FUNCTION(FunctionDeclaration.class, "module-function", false),     
   MODULE_DEFINITION(ModuleDefinition.class, "module-definition", false),    
   SCRIPT_FUNCTION(FunctionDeclaration.class, "script-function", false),
   SCRIPT_PACKAGE(ScriptPackage.class, "script-package", false),
   SCRIPT(Script.class, "script", false);
   
   public final Class type;
   public final String name;
   public final boolean trace;
   
   private Instruction(Class type, String name, boolean trace){
      this.type = type;
      this.name = name;
      this.trace = trace;
   }
   public String getName(){
      return name;
   }
   
   public Class getType(){
      return type;
   }
  
   public boolean isTrace() {
      return trace;
   } 
   
   public int getCode() {
      return ordinal();
   }
   
   public static Instruction resolveInstruction(int code) {
      Instruction[] instruction = Instruction.values();
      
      if(instruction.length > code) {
         return instruction[code];
      }
      return null;
   }
}
