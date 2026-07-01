package com.example.adwork;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ShiWuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shiwu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.ll_regulation).setOnClickListener(v ->
                startActivity(new Intent(requireActivity(), GuiZeActivity.class)));

        view.findViewById(R.id.ll_accommodation).setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), outZhuSuActivity.class);
            intent.putExtra("card_id", requireActivity().getIntent().getStringExtra("card_id"));
            startActivity(intent);
        });

        view.findViewById(R.id.ll_progress).setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ShenHeProgressActivity.class);
            intent.putExtra("card_id", requireActivity().getIntent().getStringExtra("card_id"));
            startActivity(intent);
        });
    }
}
