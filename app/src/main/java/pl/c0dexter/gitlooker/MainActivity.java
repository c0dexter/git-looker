package pl.c0dexter.gitlooker;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
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

    private static final String SEARCH_PHRASE_KEY = "search_key";
    private static final String TOOLBAR_TITLE_KEY = "title_key";
    private static final String TOOLBAR_SUBTITLE_KEY = "subtitle_key";
    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.github_repos_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frame_layout_progress_bar_container)
    FrameLayout frameLayoutProgressBarContainer;
    @BindView(R.id.welcome_picture)
    ImageView welcomeLogo;
    @BindView(R.id.progress_bar_recycler_view)
    ProgressBar progressBarResponseWaiting;
    private SearchView searchView;
    private RecyclerAdapter adapter;
    private GitRepositoryViewModel gitRepositoryViewModel;
    private String searchPhrase;
    private String toolbarTitleText;
    private String toolbarSubtitleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            searchPhrase = savedInstanceState.getString(SEARCH_PHRASE_KEY);
            toolbarSubtitleText = savedInstanceState.getString(TOOLBAR_SUBTITLE_KEY);
            toolbarTitleText = savedInstanceState.getString(TOOLBAR_TITLE_KEY);
        }

        initRecyclerView();
        showWelcomeScreen();

        gitRepositoryViewModel = ViewModelProviders.of(MainActivity.this)
                .get(GitRepositoryViewModel.class);

        gitRepositoryViewModel.getGitRepoList()
                .observe(MainActivity.this, gitRepos -> {
                    if(!gitRepos.isEmpty()){
                        adapter = new RecyclerAdapter(gitRepos, MainActivity.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(this
                                , "No result for this search criteria: "
                                , Toast.LENGTH_SHORT).show();
                    }
                });

        // Observing a data loading from the API in order to showing or hiding a progress bar
        gitRepositoryViewModel.getIsUpdating().observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressBar();
            } else {
                hideProgressBar();
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
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        setTitleAndSubtitle(toolbarTitleText, toolbarSubtitleText);

        MenuItem menuItem = menu.findItem(R.id.action_bar_search);
        searchView = (SearchView) menu.findItem(R.id.action_bar_search).getActionView();

        //focus the SearchView
        if (searchPhrase != null && !searchPhrase.isEmpty()) {
            menuItem.expandActionView();
            searchView.onActionViewExpanded();
            searchView.setQuery(searchPhrase, true);
            searchView.clearFocus();
        } else {
            searchView.setSearchableInfo(searchManager != null ? searchManager.getSearchableInfo(
                    this.getComponentName()) : null);
            searchView.setQueryHint(getString(R.string.search_view_hint));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (NetworkUtils.isOnline(MainActivity.this)) {
                    searchPhrase = query.trim();
                    searchView.setIconified(true);
                    searchView.clearFocus();
                    // After 1st search, hide a welcome pic
                    hideWelcomeScreen();
                    gitRepositoryViewModel.retrieveDataFromAPI(searchPhrase);
                    toolbar.setTitle(getString(R.string.toolbar_search_result_title));
                    toolbar.setSubtitle(searchPhrase);
                    // Collapse the action view
                    (menu.findItem(R.id.action_bar_search)).collapseActionView();
                    searchView.onActionViewCollapsed();
                    // Remove the search phrase, data has been already retrieved
                    searchPhrase = "";
                } else {
                    Toast.makeText(MainActivity.this
                            , getString(R.string.missing_internet_connection_msg)
                            , Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            // Do this only for "live search", it mean during writing action
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void setTitleAndSubtitle(String toolbarTitle, String toolbarSubtitle) {
        if (toolbarTitle != null && !toolbarTitle.isEmpty()) {
            toolbar.setTitle(toolbarTitle);
        }
        if (toolbarSubtitle != null && !toolbarSubtitle.isEmpty()) {
            toolbar.setSubtitle(toolbarSubtitle);
        }
    }

    private void showProgressBar() {
        frameLayoutProgressBarContainer.setVisibility(View.VISIBLE);
        progressBarResponseWaiting.setVisibility(View.VISIBLE);
        welcomeLogo.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        frameLayoutProgressBarContainer.setVisibility(View.GONE);
    }

    private void showWelcomeScreen() {
        frameLayoutProgressBarContainer.setVisibility(View.VISIBLE);
        progressBarResponseWaiting.setVisibility(View.GONE);
        welcomeLogo.setVisibility(View.VISIBLE);
    }

    private void hideWelcomeScreen() {
        frameLayoutProgressBarContainer.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (searchPhrase != null && !searchView.getQuery().toString().equals("")) {
            searchPhrase = searchView.getQuery().toString();
        }
        if (toolbar.getTitle() != null && !toolbar.getTitle().equals("")) {
            toolbarTitleText = toolbar.getTitle().toString();
        }
        if (toolbar.getSubtitle() != null && !toolbar.getSubtitle().equals("")) {
            toolbarSubtitleText = toolbar.getSubtitle().toString();
        }

        outState.putString(SEARCH_PHRASE_KEY, searchPhrase);
        outState.putString(TOOLBAR_TITLE_KEY, toolbarTitleText);
        outState.putString(TOOLBAR_SUBTITLE_KEY, toolbarSubtitleText);
    }
}
