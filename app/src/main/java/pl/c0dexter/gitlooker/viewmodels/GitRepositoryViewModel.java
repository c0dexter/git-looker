package pl.c0dexter.gitlooker.viewmodels;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import pl.c0dexter.gitlooker.api.models.GitRepo;
import pl.c0dexter.gitlooker.datadepository.GitRepoDataDepository;

public class GitRepositoryViewModel extends ViewModel {

    private MutableLiveData<List<GitRepo>> gitRepositories;
    private GitRepoDataDepository gitRepoDataDepository;

    public GitRepositoryViewModel() {
    }

    public void init() {
        if (gitRepositories != null) {
            return;
        }
        gitRepoDataDepository = GitRepoDataDepository.getInstance();
        gitRepositories = gitRepoDataDepository.getGitRepos();

    }

    public LiveData<List<GitRepo>> getGitRepositories() {
        return gitRepositories;
    }

}
