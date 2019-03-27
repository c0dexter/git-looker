package pl.c0dexter.gitlooker.api.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Repositories {

    @SerializedName("total_count")
    private Integer totalCount;
    @SerializedName("incomplete_results")
    private Boolean incompleteResults;
    @SerializedName("items")
    private List<GitRepo> gitRepoList = null;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Boolean getIncompleteResults() {
        return incompleteResults;
    }

    public void setIncompleteResults(Boolean incompleteResults) {
        this.incompleteResults = incompleteResults;
    }

    public List<GitRepo> getItems() {
        return gitRepoList;
    }

    public void setItems(List<GitRepo> items) {
        this.gitRepoList = items;
    }

}