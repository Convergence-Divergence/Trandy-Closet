1. buuld.gradle  추가
   - retorifit
   - Picasso
   - recycclerview
   - gson

2. AndroidManifest.xml  에 `INTERNET` permissions 추가
3. json data 생성
4. `RestroPhoto.java` 아래에 model package 추가

```java
public class RetroPhoto {

    @SerializedName("name")
    private char name;
    @SerializedName("image_number")
    private Integer image_number;
    @SerializedName("category")
    private char category;
    @SerializedName("detail")
    private char detail;
    @SerializedName("season")
    private char season;
    @SerializedName("Url")
    private char Url;

    public RetroPhoto(char name, Integer image_number, char category, char detail, char season, char Url) {
        this.name = name;
        this.image_number = image_number;
        this.category = category;
        this.detail = detail;
        this.season = season;
        this.Url = Url;
    }

    public char getname() {
        return name;
    }

    public void setname(char name) {
        this.name = name;
    }

    public Integer getimage_number() {
        return image_number;
    }

    public void setimage_number(Integer image_number) {
        this.image_number = image_number;
    }

    public char getcategory() {
        return category;
    }

    public void setcategory(char category) {
        this.category = category;
    }

    public char getdetail() {
        return detail;
    }

    public void setdetail(char detail) {
        this.detail = detail;
    }

    public char getseason() {
        return season;
    }

    public void setseason(char season) {
        this.season = season;
    }
    
      public char getUrl() {
        return Url;
    }

    public void setUrl(char Url) {
        this.Url = Url;
    }
}
```



5. retrofit 인스턴스 생성

```java
public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://w0lhdro9m0.execute-api.us-east-1.amazonaws.com/env/test";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
```



6. Endpoints 정의

```java
public interface GetDataService {

    @GET("/Cloths")
    Call<List<RetroPhoto>> getAllCloths();
}
```



7. RecycleView 와 binding data 를 위한 custom adapter  생성 (CustomAdapter.java)

```java
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private List<RetroPhoto> dataList;
    private Context context;

    public CustomAdapter(Context context,List<RetroPhoto> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtTitle;
        private ImageView coverImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = mView.findViewById(R.id.title);
            coverImage = mView.findViewById(R.id.coverImage);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.txtTitle.setText(dataList.get(position).getTitle());

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(dataList.get(position).getThumbnailUrl())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.coverImage);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
```



8. 마지막 단계
   MainActivity.java 의 onCreate() 메소드 내에서 GetDataService 인터페이스, RecyclerView, 또한 어댑토의 인스턴스를 초기화 한다.  마침내 우리는 generateDataList() 메소드를 호출한다.

```java
public class MainActivity extends AppCompatActivity {
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<RetroPhoto>> call = service.getAllPhotos();
        call.enqueue(new Callback<List<RetroPhoto>>() {
            @Override
            public void onResponse(Call<List<RetroPhoto>> call, Response<List<RetroPhoto>> response) {
                progressDoalog.dismiss();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<RetroPhoto>> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<RetroPhoto> photoList) {
        recyclerView = findViewById(R.id.customRecyclerView);
        adapter = new CustomAdapter(this,photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
```





9. 최종 산출물



![image-20201221164308451](C:\Users\i\AppData\Roaming\Typora\typora-user-images\image-20201221164308451.png)



















