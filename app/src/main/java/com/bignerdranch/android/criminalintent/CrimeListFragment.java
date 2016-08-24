package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

/**
 * Created by niedaocai on 16-6-14.
 */
public class CrimeListFragment extends Fragment {
    private static final String TAG = "CrimeListFragment";
    private static final int REQUEST_CRIME = 1;

    private static final String SUBTITLE_VISIBLE_KEY = "subtitle_visible";

    private static final boolean IS_USE_SAVE_BUTTON = false;

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mPositionClicked = -1;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE_KEY);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                newCrime();
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            case R.id.menu_item_help:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural,
                crimeCount, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void newCrime() {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getID());
        startActivity(intent);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(SUBTITLE_VISIBLE_KEY, mSubtitleVisible);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult called,requestCode=" + requestCode
                + ",resultCode=" + resultCode);
        if ((requestCode == REQUEST_CRIME) && (resultCode == Activity.RESULT_OK)) {
            UUID uuid = CrimeFragment.getUuidFromIntent(data);
            if (uuid != null) {
                //mPositionClicked = CrimeLab.get(getActivity()).getCrimeIndex(uuid);
            }
        }
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            //mAdapter.notifyDataSetChanged();
            if (IS_USE_SAVE_BUTTON) {
                //mPositionClicked is updated by onActivityResult()
                mAdapter.notifyItemChanged(mPositionClicked);
            } else {
                //mPositionClicked is updated by onClick()
                //mAdapter.notifyItemChanged(mPositionClicked);
                mAdapter.setCrimes(crimes);
                mAdapter.notifyDataSetChanged();
            }
            updateSubtitle();
        }
    }

    public class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;
        private int mPosition;

        public CrimeHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox)
                    itemView.findViewById(R.id.list_item_crime_solved_check_box);
            itemView.setOnClickListener(this);
        }

        public void bindCrime(Crime crime, int position) {
            mCrime = crime;
            mPosition = position;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View v) {
            if (IS_USE_SAVE_BUTTON) {
                //clear it for test
                mPositionClicked = -1;
            } else {
                mPositionClicked = mPosition;
            }
            //start CrimeActivity(use RecyclerView)
            //Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getID());

            //start CrimePagerActivity(use ViewPager)
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getID());
            startActivityForResult(intent, REQUEST_CRIME);
        }

    }

    public class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime, position);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }
}
