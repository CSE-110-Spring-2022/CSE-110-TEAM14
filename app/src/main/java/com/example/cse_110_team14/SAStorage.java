package com.example.cse_110_team14;

// Workaround for SearchListAdapter to call SearchActivity
public class SAStorage {
    SearchActivity sa;

    SAStorage(SearchActivity sa){
        this.sa = sa;
    }
    public void setPlanCount(int count){
        sa.setPlanCount(count);
    }
}