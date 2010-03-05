package utils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class Functional{

	public static <A,B,C> UnaryFunctor<A,C> compose(final UnaryFunctor<A,B> uf1, final UnaryFunctor<B,C> uf2){
		return new UnaryFunctor<A,C>(){public C apply(A a){return uf2.apply(uf1.apply(a));}};
	}
	public static <A,B,C,D> BinaryFunctor<A,B,D> compose(final BinaryFunctor<A,B,C> bf1, final UnaryFunctor<C,D> uf2){
		return new BinaryFunctor<A,B,D>(){public D apply(A a,B b){return uf2.apply(bf1.apply(a,b));}};
	}
	public static <A> A until(final UnaryFunctor<A,Boolean> p, final UnaryFunctor<A,A> f, A a){
		while(!p.apply(a)) a = f.apply(a);
		return a;
	}
	public static <A> Iterator<A> iterate(final UnaryFunctor<A,A> uf, final A ia){
		return new Iterator<A>(){
			A a = ia;
			public boolean hasNext(){ return true;}
			public A next(){return (a=uf.apply(a));}
			public void remove(){}
		};
	}
	public static <A> Iterator<A> repeat(final A ia){
		return new Iterator<A>(){
			public boolean hasNext(){ return true;}
			public A next(){return ia;}
			public void remove(){}
		};
	}
	public static <A> Iterator<A> cycle(final Iterable<A> iable){
		return new Iterator<A>(){
			Iterator<A> i = iable.iterator();
			public boolean hasNext(){
				if(!i.hasNext()) i = iable.iterator();
				return i.hasNext();
			}
			public A next(){
				if(!hasNext())throw new NoSuchElementException(); 
				return i.next();	 
			}
			public void remove(){}
		};
	}
	public static <A,B> List<Pair<A,B>> zip(Iterable<A> aiab, Iterable<B> biab){
		Iterator<A> ai = aiab.iterator();
		Iterator<B> bi = biab.iterator();
		List<Pair<A,B>> ret = new ArrayList<Pair<A,B>>();
		while(ai.hasNext() && bi.hasNext())
			ret.add(new Pair<A,B>(ai.next(),bi.next()));
		return ret;
	}
	public static <A,B> Pair<List<A>,List<B>> unzip(Iterable<Pair<A,B>> piab){
		Iterator<Pair<A,B>> pi = piab.iterator();
		Pair<List<A>,List<B>> ret = new Pair<List<A>,List<B>>(new ArrayList<A>(),new ArrayList<B>());
		Pair<A,B> p;
		while(pi.hasNext()){
			p = pi.next();
			ret.f.add(p.f);
			ret.s.add(p.s);
		}	
		return ret;
	}
	
	public static List mapReflect(Iterable iable,String classDotMethod){ return mapReflect(iable.iterator(),classDotMethod);}
	public static List mapReflect(Iterator it,String classDotMethod){
		List l = new ArrayList();
		if(it.hasNext()){
			Object next = it.next();
			try{
				String[] classMethod = classDotMethod.split("\\.");
				Class c = Class.forName(classMethod[0]);
				Object o = c.newInstance();
				Method m = c.getMethod(classMethod[1],next.getClass());
				l.add(m.invoke(o, next));
				while(it.hasNext()){
					l.add(m.invoke(o, it.next()));					
				}
			}catch(ClassNotFoundException e){e.printStackTrace();}
			catch(InstantiationException e){e.printStackTrace();}
			catch(IllegalAccessException e){e.printStackTrace();}
			catch(SecurityException e){e.printStackTrace();}
			catch(NoSuchMethodException e){e.printStackTrace();}
			catch(IllegalArgumentException e){e.printStackTrace();}
			catch(InvocationTargetException e){e.printStackTrace();}
		}
		return l;
	}
	
	
	public static <K, V> V mapReduce(Iterable<K> iable, UnaryFunctor<K,V> uf, BinaryFunctor<V,V,V> bf){ return mapReduce(iable.iterator(),uf,bf);}
	public static <K, V> V mapReduce(Iterator<K> it, UnaryFunctor<K,V> uf, BinaryFunctor<V,V,V> bf){ return reduce(xmap(it,uf),bf);}
	
	public static <K,V> Iterator<V> xmap(Iterable<K> iable, UnaryFunctor<K,V> uf){ return xmap(iable.iterator(),uf); }
	public static <K,V> Iterator<V> xmap(final Iterator<K> iter, final UnaryFunctor<K,V> uf){
		return new Iterator<V>(){
			Iterator<K> i = iter;
			public boolean hasNext(){ return i.hasNext();}
			public V next(){ return uf.apply(i.next());}
			public void remove(){throw new UnsupportedOperationException("Can't remove from a generator");}
		};
	}
	
	public static <K,V> List<V> map(Iterable<K> iable, UnaryFunctor<K,V> uf){ return map(iable.iterator(), uf);}
	public static <K,V> List<V> map(Iterator<K> it, UnaryFunctor<K,V> uf){
		List<V> result = new ArrayList<V>();
		while(it.hasNext()) result.add(uf.apply(it.next()));
		return result;
	}
	
	public static <V> V reduce(Iterable<V> iable, BinaryFunctor<V,V,V> bf){ return reduce(iable.iterator(),bf); }
	public static <V> V reduce(Iterator<V> it, BinaryFunctor<V,V,V> bf){
		if(!it.hasNext()) return null;
		V result = it.next();
		while(it.hasNext())	result = bf.apply(result, it.next());
		return result;
	}
	
	public static <K> List<K> filter(Iterable<K> iable, UnaryFunctor<K,Boolean> filter){ return filter(iable.iterator(), filter); }
	public static <K> List<K> filter(Iterator<K> it, UnaryFunctor<K,Boolean> filter){
		List<K> result = new ArrayList<K>();
		K k;
		while(it.hasNext()) if(filter.apply(k = it.next())) result.add(k);
		return result;
	}
	
	public static <K> Iterator<K> xfilter(Iterable<K> iable, UnaryFunctor<K,Boolean> f){ return xfilter(iable.iterator(),f); }
	public static <K> Iterator<K> xfilter(final Iterator<K> iter, final UnaryFunctor<K,Boolean> f){
		return new Iterator<K>(){
			Iterator<K> i = iter;
			K next = null;
			public boolean hasNext(){
				if(next == null){
					K temp;
					while(i.hasNext())
						if((f.apply(temp = i.next()))){
							next = temp;
							return true;
						}
					return false;
				}
				return true;
			}
			public K next(){ 
				if(next == null && !hasNext()) throw new NoSuchElementException();
				K temp = next;
				next = null;
				return temp;
			}
			public void remove(){throw new UnsupportedOperationException("Can't remove from a generator");}
		};
	}
	public void copy(int[] src, int srcpos, int[] dest, int destpos, int length){
		for (;length>0; dest[destpos+length] = src[length+srcpos], --length);
	}
}
