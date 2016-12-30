package com.ziqianfu.congresssearch;

import java.util.List;

/**
 * Created by lenovo on 2016/11/19.
 */

public class BillItem {
    public String bill_id;
    public String bill_type;
    public String official_title;
    public String introduced_on;
    public String short_title;
    public String chamber;
    public String long_title;
    public Sponsor sponsor=new Sponsor();
    public Version last_version=new Version();
    public URLPA urls=new URLPA();
    public History history=new History();
}
class BillItemList{
    public List<BillItem> results;
}
class Sponsor{
    public String first_name;
    public String last_name;
    public String title;
}
class Version{
    public URLS urls=new URLS();
    public String version_name;
}
class URLS{
    public String pdf;
}
class URLPA{
    public String congress;
}
class History{
    public String active;
}
