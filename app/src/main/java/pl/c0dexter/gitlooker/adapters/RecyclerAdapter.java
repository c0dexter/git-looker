package pl.c0dexter.gitlooker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.c0dexter.gitlooker.R;
import pl.c0dexter.gitlooker.api.models.GitRepo;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private final OnItemClickListener onItemClickListener;
    private List<GitRepo> repositoriesList = new ArrayList<>();


    public RecyclerAdapter(List<GitRepo> repositoriesList, OnItemClickListener onItemClickListener) {
        this.repositoriesList = repositoriesList;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_repo_item, parent, false);
        return new ViewHolder(view, onItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        GitRepo gitRepo = repositoriesList.get(position);

        if (gitRepo.getOwner().getAvatarUrl() != null
                && !gitRepo.getOwner().getAvatarUrl().isEmpty()) {
            Picasso.get()
                    .load(gitRepo.getOwner().getAvatarUrl())
                    .centerCrop()
                    .fit()
                    .into(holder.userAvatar, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBarAvatar.setAlpha(0f);
                            holder.progressBarAvatar.animate().setDuration(300).alpha(1f).start();
                            holder.progressBarAvatar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBarAvatar.setVisibility(View.GONE);
                        }
                    });
        } else {
            Picasso.get()
                    .load(R.drawable.github_default_avatar)
                    .centerCrop()
                    .into(holder.userAvatar, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBarAvatar.setAlpha(0f);
                            holder.progressBarAvatar.animate().setDuration(300).alpha(1f).start();
                            holder.progressBarAvatar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.progressBarAvatar.setVisibility(View.GONE);
                        }
                    });
        }

        holder.repositoryName.setText(gitRepo.getName());
        holder.userName.setText(gitRepo.getOwner().getLogin().trim());
        if (gitRepo.getLanguage() == null) {
            holder.frameProgrammingLang.setVisibility(View.GONE);
        } else {
            holder.programmingLanguageName.setText(gitRepo.getLanguage());
        }
        holder.starCounter.setText(String.valueOf(gitRepo.getStargazersCount()));
        holder.observerCounter.setText(String.valueOf(gitRepo.getWatchersCount()));
        holder.forkCounter.setText(String.valueOf(gitRepo.getForks()));

    }


    @Override
    public int getItemCount() {
        return repositoriesList.size();
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image_view_user_avatar)
        ImageView userAvatar;
        @BindView(R.id.text_view_github_user_name)
        TextView userName;
        @BindView(R.id.text_view_github_repo_name)
        TextView repositoryName;
        @BindView(R.id.text_view_programing_language_type)
        TextView programmingLanguageName;
        @BindView(R.id.image_view_github_star_icon)
        ImageView starCounterIcon;
        @BindView(R.id.text_view_github_star_counter)
        TextView starCounter;
        @BindView(R.id.image_view_github_observer_icon)
        ImageView observerCounterIcon;
        @BindView(R.id.text_view_github_observer_counter)
        TextView observerCounter;
        @BindView(R.id.image_view_github_fork_icon)
        ImageView forkCounterIcon;
        @BindView(R.id.text_view_github_fork_counter)
        TextView forkCounter;
        @BindView(R.id.progress_bar_avatar)
        ProgressBar progressBarAvatar;
        @BindView(R.id.frame_programming_lang)
        FrameLayout frameProgrammingLang;
        OnItemClickListener onItemClickListener;

        ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
