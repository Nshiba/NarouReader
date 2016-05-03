package net.nashihara.naroureader.fragments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.nashihara.naroureader.R;
import net.nashihara.naroureader.RealmUtils;
import net.nashihara.naroureader.activities.NovelViewActivity;
import net.nashihara.naroureader.adapters.SimpleRecyclerViewAdapter;
import net.nashihara.naroureader.databinding.FragmentBookmarkRecycerViewBinding;
import net.nashihara.naroureader.entities.Novel4Realm;
import net.nashihara.naroureader.listeners.OnFragmentReplaceListener;
import net.nashihara.naroureader.listeners.OnItemClickListener;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class BookmarkRecyclerViewFragment extends Fragment {
    private static final String TAG = BookmarkRecyclerViewFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private OnFragmentReplaceListener mListener;
    private Context mContext;
    private FragmentBookmarkRecycerViewBinding binding;
    private ArrayList<Novel4Realm> novels = new ArrayList<>();

    public BookmarkRecyclerViewFragment() {}

    public static BookmarkRecyclerViewFragment newInstance() {
        BookmarkRecyclerViewFragment fragment = new BookmarkRecyclerViewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark_recycer_view, container, false);

        // TODO: RecyclerView
        mRecyclerView = binding.recycler;
        final LinearLayoutManager manager = new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        manager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(manager);
        SimpleRecyclerViewAdapter adapter = new SimpleRecyclerViewAdapter(mContext);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Novel4Realm novel = novels.get(position);

                Intent intent = new Intent(mContext, NovelViewActivity.class);
                intent.putExtra("ncode", novel.getNcode());
                intent.putExtra("page", novel.getBookmark());
                intent.putExtra("title", novel.getTitle());
                intent.putExtra("writer", novel.getWriter());
                intent.putExtra("totalPage", novel.getTotalPage());

                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);

        Realm realm = RealmUtils.getRealm(mContext);
        RealmResults<Novel4Realm> results = realm.where(Novel4Realm.class).notEqualTo("bookmark", 0).findAll();

        for (Novel4Realm novel4Realm : results) {
            novels.add(novel4Realm);
        }

        adapter.addDataOf(novels);

        binding.progressBar.setVisibility(View.GONE);
        binding.recycler.setVisibility(View.VISIBLE);

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentReplaceListener) {
            mListener = (OnFragmentReplaceListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement context instanceof OnFragmentReplaceListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
