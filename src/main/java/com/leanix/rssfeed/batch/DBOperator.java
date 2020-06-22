package com.leanix.rssfeed.batch;

import java.sql.*;
import java.util.Calendar;
import java.util.Stack;

class RSSLinkPOJO {
    int id;
    String RSSLink;
    RSSLinkPOJO(int ids,String rsslink){
        id=ids;
        RSSLink=rsslink;}

}
class RSSMetaPOJO{
    int id;
    String sublink;
    String pubdate;
    String title;
    RSSMetaPOJO(){

    }
    RSSMetaPOJO(int ids,String rsslink,String pubdates ,String titl){
        id=ids;
        sublink=rsslink;
        pubdate=pubdates;
        title=titl;}
}
public class DBOperator {
    //0. Database connector
    private final String DBUrl = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "postgres";
    private Connection con=null;

    public  Connection getConnector(){
        try {
            con = DriverManager.getConnection(DBUrl, user, password);
            if (con != null) {
                //System.out.println("Successfully connected to PostgreSQL database test");
            }
        } catch (Exception ex) {
            System.out.println("An error occurred while connecting PostgreSQL database");
            ex.printStackTrace();
            System.exit(0);
        }
        return con;
    }
    public void OpenConnection(){
        con=getConnector();
    }
    //1.read data from db table
    public Stack readRSSLinks(){
        String SQL = "SELECT id,rsslink from master_rss_links where id=1";
        Stack rsslinkstack=new Stack();
        try (Connection conn = getConnector();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL);)
        {
            conn.close();
            while(rs.next())
            {
                rsslinkstack.push(new RSSLinkPOJO(rs.getInt("id"),rs.getString("rsslink")));
            }
            System.out.println("Completed Reading RSS Links from database");
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            System.exit(0);
        }
        return rsslinkstack;
    }
    //write batch entry into db table
    public int write_batch_entry() {
        con = getConnector();
        try {
            //get pub date
            java.sql.Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

            String query = String.format("INSERT into %s(start_date)values('%s') RETURNING id", "batch_info", currentTimestamp);
            ResultSet rss = con.createStatement().executeQuery(query);
            rss.next();
            con.close();
            return rss.getInt(1);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
        return 0;
    }
    //fetch unprocessed urls
    public Stack fetch_unprocessed_urls(){
        con = getConnector();
        Stack st=new Stack();
        try{
            //get pub date
            String query= String.format("Select id,url,title from %s where is_processed='0'","rss_meta_data");
            ResultSet rss=con.createStatement().executeQuery(query);
            con.close();
            while(rss.next())
            {
                st.push(new RSSMetaPOJO(rss.getInt("id"),rss.getString("url"),"", rss.getString("title")));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
        finally {
            return st;
        }

    }
    //entry to batch_log table
    public int batch_log_entry(int batch_id,int url_id){
        try {
            //get pub date
            java.sql.Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

            String query = String.format("INSERT into %s(batch_id,rss_meta_id)values(%d,%d) RETURNING id", "batch_log",batch_id,url_id);
            ResultSet rss = con.createStatement().executeQuery(query);
            rss.next();
            return rss.getInt(1);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }
        return 0;
    }
    public void batch_log_update(int id, String content,String code){
        try {
            //get pub date
            java.sql.Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());

            String query = String.format("UPDATE %s set content='%s',log_summary='%s',completion_time='%s' where id=%d", "batch_log",
                    content,code,currentTimestamp,id);
            con.createStatement().executeUpdate(query);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }

    }
    public void URLFeedFlagUpdate(int id){
        try {
            //get pub date
            String query = String.format("UPDATE %s set is_processed='1' where id=%d",
                    "rss_meta_data",id);
            con.createStatement().executeUpdate(query);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }

    }
    public void update_batch_info(int id){
        try {
            //get pub date
            java.sql.Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
            String query = String.format("UPDATE %s set end_date='%s' where id=%d",
                    "batch_info",currentTimestamp,id);
            con.createStatement().executeUpdate(query);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }

    }

    //2. write data into db table
    public void WriteRSSMeta(Stack s) {

        con = getConnector();
        System.out.println(s.size()+" urls fecthed");
        String query="nn";
        try{
            while(!s.empty())
            {
                RSSMetaPOJO rs=(RSSMetaPOJO)s.pop();

                //get pub date
                query= String.format("Select published_date from %s where url='%s'","rss_meta_data",rs.sublink);
                ResultSet rss=con.createStatement().executeQuery(query);
                if(rss.next())
                {
                    java.sql.Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTime().getTime());
                    query = String.format("UPDATE  %s set published_date='%s',is_processed='0',title='%s',last_updated_date='%s' " +
                                    "where url ='%s' and published_date<'%s'",
                            "rss_meta_data",rs.pubdate,rs.title.replace("'","''"),
                            currentTimestamp,rs.sublink,rss.getString("published_date"));
                    con.createStatement().executeUpdate(query);
                }
                else{
                    query = String.format("INSERT into  %s(url,published_date,title)values('%s','%s','%s')",
                            "rss_meta_data",rs.sublink,rs.pubdate,
                            rs.title.replace("'","''"));

                    con.createStatement().execute(query);
                }

            }


            con.close();

        }
        catch (Exception e){
            System.out.print(query);
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("Completed writing the Metadata");
    }


    public  void process(){
        Stack s=readRSSLinks();

    }


    public static void main(String s[])
    {
        DBOperator dbo=new DBOperator();
        dbo.process();
    }
}
