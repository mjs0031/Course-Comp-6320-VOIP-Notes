package ConfigControl;

public class test {
	
	public static void main(String[] args){
		
		Node n = new Node("Node 1 tux175, 10010 25 25 links 2 5 3 15 11 20 10 14");
		
			System.out.println(n.node_to_string());
			
			System.out.println("X Co: "+n.get_x_coord());
			System.out.println("Y Co: "+n.get_y_coord());	
			
			n.mutate();
			System.out.println("X Co: "+n.get_x_coord());
			System.out.println("Y Co: "+n.get_y_coord());			
			
			n.mutate();
			System.out.println("X Co: "+n.get_x_coord());
			System.out.println("Y Co: "+n.get_y_coord());	
			
			n.mutate();
			System.out.println("X Co: "+n.get_x_coord());
			System.out.println("Y Co: "+n.get_y_coord());	
			
			System.out.println(n.node_to_line());
	
	}

}
