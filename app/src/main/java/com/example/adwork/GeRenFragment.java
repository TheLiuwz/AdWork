package com.example.adwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.adwork.Tools.DBhelper;
public class GeRenFragment extends Fragment {

    private DBhelper dBhelper;
    private String cardId;

    public GeRenFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardId = requireActivity().getIntent().getStringExtra("card_id");
        dBhelper = new DBhelper(requireContext());

        loadUserInfo(view);

        // 我的事务 — 查看申请记录
        view.findViewById(R.id.ll_my_transactions).setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MyShiWuActivity.class);
            intent.putExtra("card_id", cardId);
            startActivity(intent);
        });

        // 退出登录
        view.findViewById(R.id.ll_logout).setOnClickListener(v -> {
            SharedPreferences sp = requireActivity().getSharedPreferences("login", requireActivity().MODE_PRIVATE);
            sp.edit().clear().apply();
            Toast.makeText(requireContext(), "已退出登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(requireActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void loadUserInfo(View view) {
        Cursor cursor = dBhelper.getUserInfo(cardId);
        if (cursor.moveToFirst()) {
            int idxName = cursor.getColumnIndexOrThrow("name");
            int idxClass = cursor.getColumnIndexOrThrow("class_name");
            int idxCard = cursor.getColumnIndexOrThrow("card_number");
            int idxDormBuilding = cursor.getColumnIndexOrThrow("dorm_building");
            int idxDormNumber = cursor.getColumnIndexOrThrow("dorm_number");

            ((TextView) view.findViewById(R.id.tv_name)).setText(cursor.getString(idxName));
            ((TextView) view.findViewById(R.id.tv_class)).setText(cursor.getString(idxClass));
            ((TextView) view.findViewById(R.id.tv_card_number)).setText(cursor.getString(idxCard));
            ((TextView) view.findViewById(R.id.tv_dorm_building)).setText(cursor.getString(idxDormBuilding));
            ((TextView) view.findViewById(R.id.tv_dorm_number)).setText(cursor.getString(idxDormNumber));
        }
        cursor.close();
    }
}
