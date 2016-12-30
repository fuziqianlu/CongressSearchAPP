package com.ziqianfu.congresssearch;

import java.util.List;

/**
 * Created by lenovo on 2016/11/21.
 */

public class CommitteeItem {
    public String committee_id;
    public String name;
    public String chamber;
    public String parent_committee_id;
    public String subcommittee;
    public String phone;
    public String office;
}
class CommitteeItemList{
    public List<CommitteeItem> results;
}
