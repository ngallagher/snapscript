package org.snapscript.compile;

import org.snapscript.compile.annotation.AnnotationDeclaration;
import org.snapscript.compile.annotation.AnnotationList;
import org.snapscript.compile.annotation.AnnotationName;
import org.snapscript.compile.closure.Closure;
import org.snapscript.compile.closure.ClosureParameterList;
import org.snapscript.compile.collection.Array;
import org.snapscript.compile.collection.ArrayIndex;
import org.snapscript.compile.collection.Range;
import org.snapscript.compile.condition.Choice;
import org.snapscript.compile.condition.Combination;
import org.snapscript.compile.condition.Comparison;
import org.snapscript.compile.condition.DefaultCase;
import org.snapscript.compile.condition.ForInStatement;
import org.snapscript.compile.condition.ForStatement;
import org.snapscript.compile.condition.IfStatement;
import org.snapscript.compile.condition.LoopStatement;
import org.snapscript.compile.condition.MatchStatement;
import org.snapscript.compile.condition.NullCoalesce;
import org.snapscript.compile.condition.SwitchStatement;
import org.snapscript.compile.condition.ValueCase;
import org.snapscript.compile.condition.WhileStatement;
import org.snapscript.compile.constraint.ArrayConstraint;
import org.snapscript.compile.constraint.Constraint;
import org.snapscript.compile.constraint.FunctionConstraint;
import org.snapscript.compile.constraint.ListConstraint;
import org.snapscript.compile.constraint.MapConstraint;
import org.snapscript.compile.constraint.SetConstraint;
import org.snapscript.compile.constraint.TypeConstraint;
import org.snapscript.compile.construct.ConstructArray;
import org.snapscript.compile.construct.ConstructList;
import org.snapscript.compile.construct.ConstructMap;
import org.snapscript.compile.construct.ConstructObject;
import org.snapscript.compile.construct.ConstructSet;
import org.snapscript.compile.construct.MapEntry;
import org.snapscript.compile.construct.MapEntryList;
import org.snapscript.compile.construct.MapKey;
import org.snapscript.compile.control.AssertStatement;
import org.snapscript.compile.control.BreakStatement;
import org.snapscript.compile.control.CompoundStatement;
import org.snapscript.compile.control.ContinueStatement;
import org.snapscript.compile.control.Expression;
import org.snapscript.compile.control.ExpressionStatement;
import org.snapscript.compile.control.NoOperation;
import org.snapscript.compile.control.ReturnStatement;
import org.snapscript.compile.control.Script;
import org.snapscript.compile.control.ScriptPackage;
import org.snapscript.compile.control.ThrowStatement;
import org.snapscript.compile.control.TryCatchStatement;
import org.snapscript.compile.control.TryFinallyStatement;
import org.snapscript.compile.declare.DeclarationStatement;
import org.snapscript.compile.declare.DeclareConstant;
import org.snapscript.compile.declare.DeclareVariable;
import org.snapscript.compile.define.ClassConstructor;
import org.snapscript.compile.define.ClassDefinition;
import org.snapscript.compile.define.EnumConstructor;
import org.snapscript.compile.define.EnumDefinition;
import org.snapscript.compile.define.EnumKey;
import org.snapscript.compile.define.EnumList;
import org.snapscript.compile.define.EnumValue;
import org.snapscript.compile.define.MemberField;
import org.snapscript.compile.define.MemberFunction;
import org.snapscript.compile.define.ModuleDefinition;
import org.snapscript.compile.define.ModuleFunction;
import org.snapscript.compile.define.ModuleName;
import org.snapscript.compile.define.SuperConstructor;
import org.snapscript.compile.define.ThisConstructor;
import org.snapscript.compile.define.TraitConstant;
import org.snapscript.compile.define.TraitDefinition;
import org.snapscript.compile.define.TraitFunction;
import org.snapscript.compile.define.TraitName;
import org.snapscript.compile.define.TypeHierarchy;
import org.snapscript.compile.define.TypeName;
import org.snapscript.compile.function.FunctionInvocation;
import org.snapscript.compile.function.ParameterDeclaration;
import org.snapscript.compile.function.ParameterList;
import org.snapscript.compile.function.ScriptFunction;
import org.snapscript.compile.link.FullQualifier;
import org.snapscript.compile.link.Import;
import org.snapscript.compile.link.ImportStatic;
import org.snapscript.compile.link.WildQualifier;
import org.snapscript.compile.literal.BooleanLiteral;
import org.snapscript.compile.literal.NullLiteral;
import org.snapscript.compile.literal.NumberLiteral;
import org.snapscript.compile.literal.TextLiteral;
import org.snapscript.compile.operation.Assignment;
import org.snapscript.compile.operation.AssignmentStatement;
import org.snapscript.compile.operation.CalculationList;
import org.snapscript.compile.operation.CalculationOperand;
import org.snapscript.compile.operation.CalculationOperator;
import org.snapscript.compile.operation.PostfixDecrement;
import org.snapscript.compile.operation.PostfixIncrement;
import org.snapscript.compile.operation.PrefixDecrement;
import org.snapscript.compile.operation.PrefixIncrement;
import org.snapscript.compile.operation.PrefixOperation;
import org.snapscript.compile.operation.SignedNumber;
import org.snapscript.compile.reference.ReferenceNavigation;
import org.snapscript.compile.reference.ReferencePart;
import org.snapscript.compile.reference.TypeReference;
import org.snapscript.compile.reference.TypeReferencePart;
import org.snapscript.compile.template.TextTemplate;
import org.snapscript.compile.variable.Variable;

public enum Instruction {
   DECIMAL(NumberLiteral.class, "decimal"),
   HEXIDECIMAL(NumberLiteral.class, "hexidecimal"),   
   BINARY(NumberLiteral.class, "binary"),  
   BOOLEAN(BooleanLiteral.class, "boolean"),
   IDENTIFIER(TextLiteral.class, "identifier"), // identifier 
   TEMPLATE(TextTemplate.class, "template"),
   CLASS(TextLiteral.class, "class"), // identifier
   TYPE(TextLiteral.class, "type"),  // identifier
   TEXT(TextLiteral.class, "text"),
   THIS(TextLiteral.class, "this"), // identifier
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
   COMBINATION(Combination.class, "combination"),        
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
   SWITCH_CASE(ValueCase.class, "switch-case"),
   SWITCH_DEFAULT(DefaultCase.class, "switch-default"),
   MATCH_STATEMENT(MatchStatement.class, "match-statement"),
   MATCH_CASE(ValueCase.class, "match-case"),
   MATCH_DEFAULT(DefaultCase.class, "match-default"),      
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
   TRAIT_CONSTANT(TraitConstant.class, "trait-constant"),   
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
   