package org.snapscript.core.convert;

import java.util.HashMap;
import java.util.Map;

import org.snapscript.core.Scope;
import org.snapscript.core.Type;
import org.snapscript.core.TypeExtractor;
import org.snapscript.core.TypeLoader;
import org.snapscript.core.TypeVerifier;

public class TypeMatcher {
   
   private final Map<Type, TypeConverter> converters;
   private final TypeExtractor extractor;
   private final TypeVerifier comparator;
   private final TypeConverter converter;
   
   public TypeMatcher(TypeLoader loader) {
      this.converters = new HashMap<Type, TypeConverter>();
      this.comparator = new TypeVerifier(loader);
      this.extractor = new TypeExtractor(loader);
      this.converter = new NullConverter();
   }
   
   public TypeConverter match(Type type) throws Exception { // type declared in signature
      if(type != null) {
         TypeConverter converter = converters.get(type);
         
         if(converter == null) {
            converter = resolve(type);
            converters.put(type, converter);
         }
         return converter;
      }
      return converter;
   }
   
   private TypeConverter resolve(Type type) throws Exception {
      if(comparator.same(Object.class, type)) {
         return new AnyConverter();
      }
      if(comparator.same(Number.class, type)) {
         return new NumberConverter();
      }
      if(comparator.same(Double.class, type)) {
         return new DoubleConverter();
      }
      if(comparator.same(Float.class, type)) {
         return new FloatConverter();
      }
      if(comparator.same(Integer.class, type)) {
         return new IntegerConverter();
      }
      if(comparator.same(Long.class, type)) {
         return new LongConverter();
      }
      if(comparator.same(Short.class, type)) {
         return new ShortConverter();
      }
      if(comparator.same(Byte.class, type)) {
         return new ByteConverter();
      }
      if(comparator.same(String.class, type)) {
         return new StringConverter();
      }
      if(comparator.like(Scope.class, type)) {
         return new ScopeConverter();
      }
      if(comparator.like(Enum.class, type)) {
         return new EnumConverter(extractor, type);
      }      
      return new ObjectConverter(extractor, type);
   }
}
