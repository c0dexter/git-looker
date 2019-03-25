package pl.c0dexter.gitlooker;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.c0dexter.gitlooker.adapters.RecyclerAdapter;
import pl.c0dexter.gitlooker.api.models.GitRepo;
import pl.c0dexter.gitlooker.api.models.Repositories;
import pl.c0dexter.gitlooker.api.service.ApiClient;
import pl.c0dexter.gitlooker.api.service.ApiInterface;
import pl.c0dexter.gitlooker.datadepository.GitRepoDataDepository;
import pl.c0dexter.gitlooker.utils.NetworkUtils;
import pl.c0dexter.gitlooker.viewmodels.GitRepositoryViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.github_repos_recycler_view)
    RecyclerView recyclerView;
    private Context context;
    private RecyclerAdapter adapter;
    private GitRepositoryViewModel gitRepositoryViewModel;
    private GitRepoDataDepository gitRepoDataDepository;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        ButterKnife.bind(this);
        gitRepositoryViewModel = ViewModelProviders.of(MainActivity.this).get(GitRepositoryViewModel.class);
        setGitRepositories("watch");
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }


    private void setGitRepositories(String searchQuery) {
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Repositories> call = apiInterface.repositories(searchQuery);
        call.enqueue(new Callback<Repositories>() {
            @Override
            public void onResponse(Call<Repositories> call, Response<Repositories> response) {
                if (response.isSuccessful() && response.body() != null) {
                    gitRepoDataDepository = GitRepoDataDepository.getInstance();
                    gitRepoDataDepository.setDataSet(response.body().getItems());
                    gitRepositoryViewModel = ViewModelProviders.of(MainActivity.this).get(GitRepositoryViewModel.class);
                    gitRepositoryViewModel.init();
                    adapter = new RecyclerAdapter(gitRepositoryViewModel.getGitRepositories().getValue(), MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    gitRepositoryViewModel.getGitRepositories().observe(MainActivity.this, new Observer<List<GitRepo>>() {
                        @Override
                        public void onChanged(List<GitRepo> gitRepos) {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Repositories> call, Throwable t) {
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        GitRepo gitRepo  = gitRepositoryViewModel.getGitRepositories().getValue().get(position);
        NetworkUtils.openArticleInBrowser(this, gitRepo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        return true;
    }
}
