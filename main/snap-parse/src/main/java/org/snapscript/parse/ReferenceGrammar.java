package org.snapscript.parse;

import org.snapscript.common.Cache;
import org.snapscript.common.LeastRecentlyUsedCache;

public class ReferenceGrammar implements Grammar {

   private final Cache<Integer, GrammarMatcher> matchers;
   private final ReferenceBuilder resolver; 
   
   public ReferenceGrammar(GrammarResolver resolver, String name, int index) {
      this(resolver, name, index, 100);
   }
   
   public ReferenceGrammar(GrammarResolver resolver, String name, int index, int capacity) {
      this.matchers = new LeastRecentlyUsedCache<Integer, GrammarMatcher>(capacity);
      this.resolver = new ReferenceBuilder(resolver, name, index);
   }   
   
   @Override
   public GrammarMatcher create(int serial) {
      GrammarMatcher matcher = matchers.fetch(serial); 
      
      if(matcher == null) {
         matcher = resolver.create(serial);
         matchers.cache(serial, matcher);
      }
      return matcher;
   }  
}
