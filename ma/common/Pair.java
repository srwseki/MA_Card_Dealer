package ma.common;

public class Pair<T1, T2> {
	public T1 first;
	public T2 second;
	
	public Pair(T1 o1,  T2 o2) {
		this.first = o1;
		this.second = o2;
	}
	
	public String toString() {
		String f = first==null?"null":first.toString();
		String s = second==null?"null":second.toString();
		return "("+f+","+s+")";
	}
}
