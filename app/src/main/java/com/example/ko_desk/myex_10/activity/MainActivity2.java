package com.example.ko_desk.myex_10.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ko_desk.myex_10.HttpClient;
import com.example.ko_desk.myex_10.R;
import com.example.ko_desk.myex_10.Web;
import com.example.ko_desk.myex_10.data.Constant;
import com.example.ko_desk.myex_10.navigationdrawer.NavMenuAdapter;
import com.example.ko_desk.myex_10.navigationdrawer.NavMenuModel;
import com.example.ko_desk.myex_10.navigationdrawer.SubTitle;
import com.example.ko_desk.myex_10.navigationdrawer.TitleMenu;
import com.example.ko_desk.myex_10.vo.Data;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity implements NavMenuAdapter.MenuItemClickListener{


    Toolbar toolbar;
    DrawerLayout drawer;
    ArrayList<NavMenuModel> menu;
    InnerTask task = null;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setToolbar();

        drawer = (DrawerLayout) findViewById(R.id.main_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setNavigationDrawerMenu();

        Intent intent = getIntent();
        id = intent.getStringExtra("id"); //req.getParameter("id")'

        task = new MainActivity2.InnerTask();
        task.execute(id);

    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void setNavigationDrawerMenu() {
        NavMenuAdapter adapter = new NavMenuAdapter(this, getMenuList(), this);
        RecyclerView navMenuDrawer = (RecyclerView) findViewById(R.id.main_nav_menu_recyclerview);
        navMenuDrawer.setAdapter(adapter);
        navMenuDrawer.setLayoutManager(new LinearLayoutManager(this));
        navMenuDrawer.setAdapter(adapter);

//        INITIATE SELECT MENU
        adapter.selectedItemParent = menu.get(0).menuTitle;
        onMenuItemClick(adapter.selectedItemParent);
        adapter.notifyDataSetChanged();
    }


    private List<TitleMenu> getMenuList() {
        List<TitleMenu> list = new ArrayList<>();

        menu = Constant.getMenuNavigasi();
        for (int i = 0; i < menu.size(); i++) {
            ArrayList<SubTitle> subMenu = new ArrayList<>();
            if (menu.get(i).subMenu.size() > 0){
                for (int j = 0; j < menu.get(i).subMenu.size(); j++) {
                    subMenu.add(new SubTitle(menu.get(i).subMenu.get(j).subMenuTitle));
                }
            }

            list.add(new TitleMenu(menu.get(i).menuTitle, subMenu, menu.get(i).menuIconDrawable));
        }

        return list;
    }

    @Override
    public void onMenuItemClick(String itemString) {
        for (int i = 0; i < menu.size(); i++) {
            if (itemString.equals(menu.get(i).menuTitle)){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, menu.get(i).fragment)
                        .commit();
                break;
            }else{
                for (int j = 0; j < menu.get(i).subMenu.size(); j++) {
                    if (itemString.equals(menu.get(i).subMenu.get(j).subMenuTitle)){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.main_content, menu.get(i).subMenu.get(j).fragment)
                                .commit();
                        break;
                    }
                }
            }
        }

        if (drawer != null){
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        new AlertDialog.Builder(this)
                .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent i = new Intent(MainActivity2.this, SignInActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(i);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();

        if (id == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class InnerTask extends AsyncTask {

        //MypageRecyAdapter adapter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient.Builder http = new HttpClient.Builder("POST", Web.servletURL + "android/androidMyPageMain"); //@RequestMapping url
            http.addOrReplace("id", (String) objects[0]);

            HttpClient post = http.create();
            post.request();

            String body = post.getBody();
            return body;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d("JSON_RESULT", (String) o);
            Gson gson = new Gson();
            Data data = gson.fromJson((String) o, Data.class);
            try {
                TextView name = (TextView) findViewById(R.id.name);
                TextView className = (TextView) findViewById(R.id.className);
                ImageView imageView =  (ImageView) findViewById(R.id.stuimg);
                name.setText(data.getData2() + " 님");
                className.setText(data.getData3());
                String imageUrl = "http://choihyun7000.synology.me/profile_photo/student/"+data.getData4();
                Glide.with(MainActivity2.this).load(imageUrl).into(imageView);
                Log.d("pic", "사진 = " + data.getData4());
                Log.d("JSON_RESULT", "이름 = " + data.getMember().get("member_name"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
