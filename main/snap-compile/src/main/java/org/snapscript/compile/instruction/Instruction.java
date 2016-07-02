package org.snapscript.compile.instruction;

import org.snapscript.compile.instruction.collection.Array;
import org.snapscript.compile.instruction.collection.ArrayIndex;
import org.snapscript.compile.instruction.collection.Range;
import org.snapscript.compile.instruction.condition.Choice;
import org.snapscript.compile.instruction.condition.Comparison;
import org.snapscript.compile.instruction.condition.ConditionalList;
import org.snapscript.compile.instruction.condition.ConditionalOperand;
import org.snapscript.compile.instruction.condition.ConditionalOperator;
import org.snapscript.compile.instruction.condition.ConditionalResult;
import org.snapscript.compile.instruction.condition.ForInStatement;
import org.snapscript.compile.instruction.condition.ForStatement;
import org.snapscript.compile.instruction.condition.IfStatement;
import org.snapscript.compile.instruction.condition.LoopStatement;
import org.snapscript.compile.instruction.condition.NullCoalesce;
import org.snapscript.compile.instruction.condition.WhileStatement;
import org.snapscript.compile.instruction.construct.ConstructArray;
import org.snapscript.compile.instruction.construct.ConstructList;
import org.snapscript.compile.instruction.construct.ConstructMap;
import org.snapscript.compile.instruction.construct.ConstructObject;
import org.snapscript.compile.instruction.construct.ConstructSet;
import org.snapscript.compile.instruction.construct.MapEntry;
import org.snapscript.compile.instruction.construct.MapEntryList;
import org.snapscript.compile.instruction.construct.MapKey;
import org.snapscript.compile.instruction.define.ClassConstructor;
import org.snapscript.compile.instruction.define.ClassDefinition;
import org.snapscript.compile.instruction.define.EnumConstructor;
import org.snapscript.compile.instruction.define.EnumDefinition;
import org.snapscript.compile.instruction.define.EnumKey;
import org.snapscript.compile.instruction.define.EnumList;
import org.snapscript.compile.instruction.define.EnumValue;
import org.snapscript.compile.instruction.define.MemberField;
import org.snapscript.compile.instruction.define.MemberFunction;
import org.snapscript.compile.instruction.define.ModuleDefinition;
import org.snapscript.compile.instruction.define.ModuleFunction;
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
   DECIMAL(NumberLiteral.class, "decimal"),
   HEXIDECIMAL(NumberLiteral.class, "hexidecimal"),   
   BINARY(NumberLiteral.class, "binary"),  
   BOOLEAN(BooleanLiteral.class, "boolean"),
   IDENTIFIER(TextLiteral.class, "identifier"), 
   TEMPLATE(TextTemplate.class, "template"),
   CLASS(TextLiteral.class, "class"),  
   TYPE(TextLiteral.class, "type"),   
   TEXT(TextLiteral.class, "text"),
   THIS(TextLiteral.class, "this"),
   NULL(NullLiteral.class, "null"),
   NUMBER(SignedNumber.class, "number"), 
   VARIABLE(Variable.class, "variable"),
   ARGUMENT(Argument.class, "argument"),
   RANGE(Range.class, "range"),     
   ARRAY(Array.class, "array"),      
   ARRAY_INDEX(ArrayIndex.class, "array-index"),                
   FUNCTION_INVOCATION(FunctionInvocation.class, "function-invocation"),           
   ARGUMENT_LIST(ArgumentList.class, "argument-list"),     
   REFERENCE_NAVIGATION(ReferenceNavigation.class, "reference-navigation"), 
   REFERENCE_TYPE(ReferenceNavigation.class, "reference-type"),   
   REFERENCE_PART(ReferencePart.class, "reference-part"),
   CALCULATION_LIST(CalculationList.class, "calculation-list"),
   CALCULATION_OPERATOR(CalculationOperator.class, "calculation-operator"),
   CALCULATION_OPERAND(CalculationOperand.class, "calculation-operand"),   
   COMPARISON(Comparison.class, "comparison"),
   CONDITIONAL_LIST(ConditionalList.class, "conditional-list"),
   CONDITIONAL_OPERATOR(ConditionalOperator.class, "conditional-operator"),
   CONDITIONAL_OPERAND(ConditionalOperand.class, "conditional-operand"),   
   CONDITIONAL_RESULT(ConditionalResult.class, "conditional-result"),      
   POSTFIX_INCREMENT(PostfixIncrement.class, "postfix-increment"),
   POSTFIX_DECREMENT(PostfixDecrement.class, "postfix-decrement"),
   PREFIX_INCREMENT(PrefixIncrement.class, "prefix-increment"),
   PREFIX_DECREMENT(PrefixDecrement.class, "prefix-decrement"),
   PREFIX_OPERATION(PrefixOperation.class, "prefix-operation"),   
   CHOICE(Choice.class, "choice"), 
   NULL_COALESCE(NullCoalesce.class, "null-coalesce"),    
   ASSIGNMENT(Assignment.class, "assignment"),
   TYPE_REFERENCE(TypeReference.class, "type-reference"),
   TYPE_REFERENCE_PART(TypeReferencePart.class, "type-reference-part"),   
   CONSTRUCT_LIST(ConstructList.class, "construct-list"),
   CONSTRUCT_ARRAY(ConstructArray.class, "construct-array"),
   CONSTRUCT_OBJECT(ConstructObject.class, "construct-object"),
   CONSTRUCT_MAP(ConstructMap.class, "construct-map"),   
   CONSTRUCT_SET(ConstructSet.class, "construct-set"),
   LIST_ENRTY_LIST(ArgumentList.class, "list-entry-list"),
   LIST_ENTRY(Argument.class, "list-entry"),
   SET_ENRTY_LIST(ArgumentList.class, "set-entry-list"),
   SET_ENTRY(Argument.class, "set-entry"),
   MAP_ENRTY_LIST(MapEntryList.class, "map-entry-list"),
   MAP_ENTRY(MapEntry.class, "map-entry"),
   MAP_KEY(MapKey.class, "map-key"),
   DECLARE_VARIABLE(DeclareVariable.class, "declare-variable"),
   DECLARE_CONSTANT(DeclareConstant.class, "declare-constant"),      
   DECLARATION_STATEMENT(DeclarationStatement.class, "declaration-statement"),
   ASSIGNMENT_STATEMENT(AssignmentStatement.class, "assignment-statement"),      
   EXPRESSION_STATEMENT(ExpressionStatement.class, "expression-statement"),
   TERMINAL_STATEMENT(NoOperation.class, "terminal-statement"),   
   BLANK_STATEMENT(NoOperation.class, "empty-statement"),
   COMPOUND_STATEMENT(CompoundStatement.class, "compound-statement"),   
   IF_STATEMENT(IfStatement.class, "if-statement"),
   BREAK_STATEMENT(BreakStatement.class, "break-statement"),
   CONTINUE_STATEMENT(ContinueStatement.class, "continue-statement"),
   RETURN_STATEMENT(ReturnStatement.class, "return-statement"),      
   ASSERT_STATEMENT(AssertStatement.class, "assert-statement"),    
   WHILE_STATEMENT(WhileStatement.class, "while-statement"),
   FOR_STATEMENT(ForStatement.class, "for-statement"),
   FOR_IN_STATEMENT(ForInStatement.class, "for-in-statement"),
   LOOP_STATEMENT(LoopStatement.class, "loop-statement"),
   SWITCH_STATEMENT(SwitchStatement.class, "switch-statement"),
   VALUE_CASE(ValueCase.class, "value-case"),
   DEFAULT_CASE(DefaultCase.class, "default-case"),
   FUNCTION_CONSTRAINT(FunctionConstraint.class, "function-constraint"),
   TYPE_CONSTRAINT(TypeConstraint.class, "type-constraint"),
   ARRAY_CONSTRAINT(ArrayConstraint.class, "array-constraint"),
   LIST_CONSTRAINT(ListConstraint.class, "list-constraint"),   
   SET_CONSTRAINT(SetConstraint.class, "set-constraint"),   
   MAP_CONSTRAINT(MapConstraint.class, "map-constraint"),   
   CONSTRAINT(Constraint.class, "constraint"),   
   VARIABLE_ARGUMENT(Modifier.class, "variable-argument"),
   PARAMETER(ParameterDeclaration.class, "parameter-declaration"),
   PARAMETER_LIST(ParameterList.class, "parameter-list"),
   CLOSURE_PARAMETER_LIST(ClosureParameterList.class, "closure-parameter-list"),
   CLOSURE(Closure.class, "closure"),
   THROW(ThrowStatement.class, "throw-statement"),    
   TRY_CATCH(TryCatchStatement.class, "try-catch-statement"),
   TRY_FINALLY(TryFinallyStatement.class, "try-finally-statement"),     
   TYPE_NAME(TypeName.class, "type-name"),
   TRAIT_NAME(TraitName.class, "trait-name"),     
   TRAIT_HIERARCHY(TypeHierarchy.class, "trait-hierarchy"),
   TRAIT_DEFINITION(TraitDefinition.class, "trait-definition"),    
   TRAIT_FUNCTION(TraitFunction.class, "trait-function"),
   ENUM_KEY(EnumKey.class, "enum-key"),
   ENUM_HIERARCHY(TypeHierarchy.class, "enum-hierarchy"),
   ENUM_FIELD(MemberField.class, "enum-field"),
   ENUM_FUNCTION(MemberFunction.class, "enum-function"),   
   ENUM_DEFINITION(EnumDefinition.class, "enum-definition"), 
   ENUM_CONSTRUCTOR(EnumConstructor.class, "enum-constructor"), 
   ENUM_VALUE(EnumValue.class, "enum-value"),
   ENUM_LIST(EnumList.class, "enum-list"),     
   CLASS_HIERARCHY(TypeHierarchy.class, "class-hierarchy"),
   CLASS_DEFINITION(ClassDefinition.class, "class-definition"),
   CLASS_FIELD(MemberField.class, "class-field"),
   CLASS_FUNCTION(MemberFunction.class, "class-function"),   
   CLASS_CONSTRUCTOR(ClassConstructor.class, "class-constructor"),  
   ANNOTATION_NAME(AnnotationName.class, "annotation-name"),
   ANNOTATION_LIST(AnnotationList.class, "annotation-list"),
   ANNOTATION_DECLARATION(AnnotationDeclaration.class, "annotation-declaration"),
   FIELD_MODIFIER(Modifier.class, "field-modifier"),
   FIELD_MODIFIER_LIST(ModifierList.class, "field-modifier-list"),
   FUNCTION_MODIFIER(Modifier.class, "function-modifier"),
   FUNCTION_MODIFIER_LIST(ModifierList.class, "function-modifier-list"),
   ACCESS_MODIFIER(Modifier.class, "access-modifier"),
   ACCESS_MODIFIER_LIST(ModifierList.class, "access-modifier-list"), 
   SUPER_CONSTRUCTOR(SuperConstructor.class, "super-constructor"),
   THIS_CONSTRUCTOR(ThisConstructor.class, "this-constructor"),
   EXPRESSION(Expression.class, "expression"),
   WILD_QUALIFIER(WildQualifier.class, "wild-qualifier"),
   FULL_QUALIFIER(FullQualifier.class, "full-qualifier"),
   IMPORT(Import.class, "import"), 
   IMPORT_STATIC(ImportStatic.class, "import-static"),
   MODULE_NAME(ModuleName.class, "module-name"),    
   MODULE_FUNCTION(ModuleFunction.class, "module-function"),     
   MODULE_DEFINITION(ModuleDefinition.class, "module-definition"),    
   SCRIPT_FUNCTION(ScriptFunction.class, "script-function"),
   SCRIPT_PACKAGE(ScriptPackage.class, "script-package"),
   SCRIPT(Script.class, "script");
   
   public final Class type;
   public final String name;
   
   private Instruction(Class type, String name){
      this.type = type;
      this.name = name;
   }

   public String getName(){
      return name;
   }
   
   public Class getType(){
      return type;
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
