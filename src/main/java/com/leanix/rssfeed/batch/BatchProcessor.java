package com.leanix.rssfeed.batch;

import java.util.Stack;

public class BatchProcessor {
    int batch_id;
    DBOperator dbo=new DBOperator();

    //0. create batch id
    public void create_batch(){
        // write entry into db
        batch_id=dbo.write_batch_entry();
        System.out.println("batch entry created");
        //System.out.println(batch_id);

    }
    public void end_batch(){
        dbo.update_batch_info(batch_id);
        System.out.println("batch ended");
        //System.out.println(batch_id);
    }


    public void list_rss_suburl(Stack st){
        RSSMetaPOJO rss_mt;

        try{
            dbo.OpenConnection();
            while(!st.empty()){
                rss_mt=(RSSMetaPOJO)st.pop();
                int log_id=dbo.batch_log_entry(batch_id,rss_mt.id);
                String code=scrape_url(rss_mt.title);
                String content="ll";
                if (code=="Yes"){
                    content="Success log";
                }
                else{
                    content="Error log";}
                dbo.batch_log_update(log_id,content,code);
                dbo.URLFeedFlagUpdate(rss_mt.id);

            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            //e.printStackTrace();
        }

    }
    public String scrape_url(String title){
        System.out.println(title.length());
        if (title.length()>60) return "No";
        return "Yes";
    }

    // 2. process the url sand write entry into batch log table
    public void process(){
        System.out.println("Starting batch to process each url");
        create_batch();
        //1. fetch urls where the feed_process flag is '0'
        Stack s=dbo.fetch_unprocessed_urls();
        System.out.println("Fetched the urls to be processed");
        if(s.empty()){
            System.out.println("No urls to process");
        }
        else{
            list_rss_suburl(s);}
        dbo.OpenConnection();
        end_batch();



    }
    public static void main(String s[])
    {
        FetchRSSMetadata rssmeta=new FetchRSSMetadata();
        rssmeta.process();
        System.out.println("Completed reading links");
        BatchProcessor bp=new BatchProcessor();
        bp.process();


    }
}
