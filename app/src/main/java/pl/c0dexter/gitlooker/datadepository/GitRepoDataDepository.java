package pl.c0dexter.gitlooker.datadepository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import pl.c0dexter.gitlooker.api.models.GitRepo;

/**
 * Singleton pattern, it will be used to API connection
 */
public class GitRepoDataDepository {

    private static GitRepoDataDepository instance;
    private List<GitRepo> dataSet = new ArrayList<>();


    public static GitRepoDataDepository getInstance() {
        if (instance == null) {
            instance = new GitRepoDataDepository();
        }
        return instance;
    }

    public void setDataSet(List<GitRepo> dataSet) {
        this.dataSet = dataSet;
    }

    public MutableLiveData<List<GitRepo>> getGitRepos() {
        MutableLiveData<List<GitRepo>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }


}
