package org.snapscript.compile.instruction;

import org.snapscript.compile.instruction.collection.Array;
import org.snapscript.compile.instruction.collection.ArrayIndex;
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
import org.snapscript.compile.instruction.define.TraitDefinition;
import org.snapscript.compile.instruction.define.TraitFunction;
import org.snapscript.compile.instruction.define.TraitName;
import org.snapscript.compile.instruction.define.TypeHierarchy;
import org.snapscript.compile.instruction.define.TypeName;
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

public enum Instruction {
   DECIMAL(NumberLiteral.class, "decimal"),
   HEXIDECIMAL(NumberLiteral.class, "hexidecimal"),    
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
   ARRAY(Array.class, "array"),      
   ARRAY_INDEX(ArrayIndex.class, "array-index"),                
   FUNCTION_INVOCATION(FunctionInvocation.class, "function-invocation"),           
   ARGUMENT_LIST(ArgumentList.class, "argument-list"),     
   REFERENCE_LIST(ReferenceList.class, "reference-list"),
   REFERENCE_PART(ReferencePart.class, "reference-part"),
   CALCULATION_LIST(CalculationList.class, "calculation-list"),
   CALCULATION_OPERATOR(CalculationOperator.class, "calculation-operator"),
   CALCULATION_OPERAND(CalculationOperand.class, "calculation-operand"),   
   COMPARISON(Comparison.class, "comparison"),
   CONDITIONAL_LIST(ConditionalList.class, "conditional-list"),
   CONDITIONAL_OPERATOR(ConditionalOperator.class, "conditional-operator"),
   CONDITIONAL_OPERAND(ConditionalOperand.class, "conditional-operand"),   
   POSTFIX_INCREMENT(PostfixIncrement.class, "postfix-increment"),
   POSTFIX_DECREMENT(PostfixDecrement.class, "postfix-decrement"),
   PREFIX_INCREMENT(PrefixIncrement.class, "prefix-increment"),
   PREFIX_DECREMENT(PrefixDecrement.class, "prefix-decrement"),
   PREFIX_OPERATION(PrefixOperation.class, "prefix-operation"),   
   CHOICE(Choice.class, "choice"), 
   ASSIGNMENT(Assignment.class, "assignment"),
   CONSTRUCT_ARRAY(ConstructArray.class, "construct-array"),
   CONSTRUCT_LIST(ConstructList.class, "construct-list"),
   CONSTRUCT_OBJECT_ARRAY(ConstructObjectArray.class, "construct-object-array"),
   CONSTRUCT_OBJECT(ConstructObject.class, "construct-object"),
   CONSTRUCT_MAP(ConstructMap.class, "construct-map"),   
   CONSTRUCT_SET(ConstructSet.class, "construct-set"),
   ARRAY_ENRTY_LIST(ArgumentList.class, "array-entry-list"),
   ARRAY_ENTRY(Argument.class, "array-entry"),
   SET_ENRTY_LIST(ArgumentList.class, "set-entry-list"),
   SET_ENTRY(Argument.class, "set-entry"),
   MAP_ENRTY_LIST(MapEntryList.class, "map-entry-list"),
   MAP_ENTRY(MapEntry.class, "map-entry"),
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
   WHILE_STATEMENT(WhileStatement.class, "while-statement"),
   FOR_STATEMENT(ForStatement.class, "for-statement"),
   FOR_INFINITE_STATEMENT(ForInfiniteStatement.class, "for-infinite-statement"),
   FOR_IN_STATEMENT(ForInStatement.class, "for-in-statement"),
   TYPE_CONSTRAINT(Constraint.class, "type-constraint"),
   PARAMETER(Parameter.class, "parameter"),
   PARAMETER_LIST(ParameterList.class, "parameter-list"),
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
   ENUM_DEFINITION(EnumDefinition.class, "enum-definition"), 
   ENUM_VALUE(EnumValue.class, "enum-value"),
   ENUM_LIST(EnumList.class, "enum-list"),     
   CLASS_HIERARCHY(TypeHierarchy.class, "class-hierarchy"),
   CLASS_DEFINITION(ClassDefinition.class, "class-definition"),   
   MODIFIER(Modifier.class, "modifier"),
   MODIFIER_LIST(ModifierList.class, "modifier-list"), 
   MEMBER_FIELD(MemberField.class, "member-field"),
   MEMBER_FUNCTION(MemberFunction.class, "member-function"),   
   MEMBER_CONSTRUCTOR(MemberConstructor.class, "member-constructor"),        
   EXPRESSION(Expression.class, "expression"),
   WILD_QUALIFIER(WildQualifier.class, "wild-qualifier"),
   FULL_QUALIFIER(FullQualifier.class, "full-qualifier"),
   IMPORT(Import.class, "import"), 
   IMPORT_STATIC(ImportStatic.class, "import-static"),
   MODULE_NAME(ModuleName.class, "module-name"),    
   MODULE_FUNCTION(FunctionDeclaration.class, "module-function"),     
   MODULE_DEFINITION(ModuleDefinition.class, "module-definition"),    
   SCRIPT_FUNCTION(FunctionDeclaration.class, "script-function"),     
   SCRIPT(Script.class, "script"),
   LIBRARY(Script.class, "library");
   
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