package pl.c0dexter.gitlooker;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.c0dexter.gitlooker.adapters.RecyclerAdapter;
import pl.c0dexter.gitlooker.api.models.GitRepo;
import pl.c0dexter.gitlooker.depository.GitDataDepository;
import pl.c0dexter.gitlooker.utils.NetworkUtils;
import pl.c0dexter.gitlooker.viewmodels.GitRepositoryViewModel;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.github_repos_recycler_view)
    RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private GitRepositoryViewModel gitRepositoryViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initRecyclerView();

        gitRepositoryViewModel = ViewModelProviders.of(this)
                .get(GitRepositoryViewModel.class);
        gitRepositoryViewModel.getGitRepoList("calculator")
                .observe(this, new Observer<List<GitRepo>>() {
                    @Override
                    public void onChanged(List<GitRepo> gitRepos) {
                        adapter = new RecyclerAdapter(gitRepos, MainActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                });

    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onItemClick(View view, int position) {
        GitRepo gitRepo = Objects.requireNonNull(GitDataDepository
                .getInstance()
                .getGitRepos()
                .getValue())
                .get(position);
        NetworkUtils.openArticleInBrowser(this, gitRepo);
        Log.d(TAG, "Clicked repo on the #" + position + ", repo name: " + gitRepo.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        return true;
    }

}
