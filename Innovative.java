import java.sql.*; 
import java.util.*; 
class Innovative{  
    public static void main(String args[]){  
        try{  
        Class.forName("com.mysql.jdbc.Driver");  
        Connection con=DriverManager.getConnection(  
        "jdbc:mysql://localhost:3306/innovative","root","");  
        //here innovative is a database name and root is username 
       
        //Scanning the name of the table from the user
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the name of the table: ");
        String table=sc.nextLine();

        //Finding total number of columns in the emp table.
        Statement stmt=con.createStatement();  
        ResultSet noc=stmt.executeQuery("select count(COLUMN_NAME) from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME='"+table+"'");  
        noc.next();
        int columns=noc.getInt(1);
        //System.out.println(columns);

        //Displaying the table
        System.out.println("---------------------------------TABLE---------------------------------");
        System.out.println();
        ResultSet tab=stmt.executeQuery("select * from "+table+";");
        while(tab.next()){
            for (int i = 1; i <= columns; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = tab.getString(i);
                System.out.print(columnValue);
            }
            System.out.println("");
        }  

        //Making an array of all the  column names.
        String cols[]=new String[columns];
        int i=0;
        ResultSet rs=stmt.executeQuery("select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME='"+table+"'");  
        while(rs.next()){  
        cols[i++]=(rs.getString(1));
        }

        //Finding total number of rows in the table.
        ResultSet r=stmt.executeQuery("select count(*) from "+table+";"); 
        r.next();
        int rows=r.getInt(1);
        //System.out.println(rows); 

        //List for keys
        List<String>cand=new ArrayList<String>();
        List<String>uni=new ArrayList<String>();

        System.out.println();
        System.out.println("-------------------------KEYS IDENTIFICATION-------------------------");
        //Identifying keys by checking each columns.
        for(int j=0;j<cols.length;j++){
            //Finding total number of null rows in a praticular column.
            ResultSet nullc=stmt.executeQuery("select count(*) from "+table+" where "+ cols[j]+" is NULL;");
            nullc.next();
            int nullcnt=nullc.getInt(1);

            //Finding total number of distinct rows excluding null rows
            ResultSet drc=stmt.executeQuery("select count(distinct "+cols[j]+") from "+table+";");
            drc.next();
            int disrows=drc.getInt(1);
            
            if(nullcnt==0){
                //Candidate key
                if(rows==disrows){
                    cand.add(cols[j]);
                }
            }
            else{
                //unique key
                int temp=rows-nullcnt;
                if(temp==disrows){
                    uni.add(cols[j]);
                }
            }
        }

        //Printing candidate keys, primary key and alternate key
        System.out.println();
        System.out.println("Candidate Keys: "+cand);
        System.out.println();
        for(int m=0;m<cand.size();m++){
            System.out.print("Primary key: "+cand.get(m)+", Alternate Keys: ");
            for(int n=0;n<cand.size();n++){
                if(n==m){
                    continue;
                }
                System.out.print(" "+cand.get(n));
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("Super Key: It is a key that consists of one or more attributes which can uniquely identify a row in a table. It is a superset of a candidate key.");
        System.out.println();

        //Printing Unique keys
        System.out.println("Unique Keys: "+uni);
        System.out.println();
        con.close();  
        }
        catch(Exception e){ System.out.println(e);}  
    }  
}