# SoundHub
- Introduction
  1. SoundHub is the music platform combining each sound like Github
  2. Period: Nov.27, 2017 ~ Dec.31, 2017 (about a month)
- Purpose
  1. Understand and apply MVVM pattern and Data binding
  2. Understand and apply Git flow 
  3. Understand client's UI/UX

- Cooperation tools
  1. TODO Check: Googe docs (http://bit.ly/2E6to0D)
  2. Daily scrum: Google Hangount
  
- Testing
  1. Video: https://www.youtube.com/watch?v=toBCfZmIJk0&t=7s (Korean version)
  2. Doc: https://www.slideshare.net/HeeJuMun/soundhub (Korean version)

## Architecture (MVVM)
- Why we use architecture?
  1. More easily maintenance
  2. Face Spaghetti Code Problems when modify the code
- Why we selected MVVM pattern?
  1. View - ViewModel (N:1). There is no dependence on each other
  2. Need the experience using MVVM

## ScreenShot
- Login (Normal Login, Social Login)
![login](https://github.com/Heepie/SoundHub/blob/develop/img/screen_shot1.png)
- Home (Navigation View, Recycler View)
![home](https://github.com/Heepie/SoundHub/blob/develop/img/screen_shot2.png)
- Detail Post (Play music simultaneously, Request music mixing, Upload recode, Update file)
![detail](https://github.com/Heepie/SoundHub/blob/develop/img/screen_shot3.png)

## Code code
### Heepie
- ### MVVM, Data Binding
![flow](https://github.com/Heepie/SoundHub/blob/develop/img/mvvm_flow_watermark.png)</br> 
  1. Networking DataAPI with Server
  ```Java
  public Observable<Response<Data>> getData(String category) {
      // Set Retrofit
      IData service = retrofit.create(IData.class);

      // Set RESTFul
      if (category == Const.CATEGORY_DEFAULT)
          return service.getData("");

      return service.getData(category);
  }
  ```
  2. Networking DataAPI with ViewModel
  ```Java
  public void setDisplayData(String category, ICallback callback) {
      dataAPI = DataAPI.getInstance();
      Observable<Response<Data>> dataObs = dataAPI.getData(category);

      // Subscribe Data by RxJava
      dataObs.subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(
                  jsonData -> {
                  // Set data to DataModel
                  dataAPI.setModelData(jsonData.body());
                  data = jsonData.body();
                  setData(data);
                  callback.initData(Const.RESULT_SUCCESS, "Success", null);
              });
  }
  ```
  3. Networking View with ViewModel
  ```Java
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // Init Binding
      listBinding = DataBindingUtil.setContentView(this, R.layout.list_view);
      listViewModel = new ListViewModel(this);

      // ViewModel Binding
      listBinding.setVariable(BR.viewModel, listViewModel);
      listBinding.setVariable(BR.view, this);
  }

  private void initData(String category, ICallback callback) {
      listBinding.progressBar.setVisibility(View.VISIBLE);
      listViewModel.resetData();

      // Call business logic on ViewModel
      listViewModel.setDisplayData(category, callback);
      listViewModel.setDisplayCategory();
  }
  ```
  4. Set View
  ```XML
  <?xml version="1.0" encoding="utf-8"?>
  <layout
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    >

    <data>
        <import type="android.R"/>
        <variable
            name="view"
            type="com.heepie.soundhub.view.ListView"/>
        <!-- Set ViewModel variable -->
        <variable
            name="viewModel"
            type="com.heepie.soundhub.viewmodel.ListViewModel"/>
    </data>

    <!-- Data Binding by BindingAdapter -->
    <android.support.v7.widget.RecyclerView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/black"
        app:activity="@{view}"
        app:setOuterViewModel="@{viewModel}">
  </layout>
  ```
- ### Material Design
    1. XML
    ```XML
    <!-- transitionName for sharing a view ->
    <ImageButton
        android:src="@drawable/comments"
        android:transitionName="@string/sharedComment"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@+id/list_comment_count"
        app:layout_constraintTop_toTopOf="parent"/>
    ```
    2. Java
    ```Java
    // Connect sharing a view and transitionName
    Pair<View, String> pair1 = Pair.create(image, image.getTransitionName());
    Pair<View, String> pair2 = Pair.create(like, like.getTransitionName());
    Pair<View, String> pair3 = Pair.create(comment, comment.getTransitionName());

    // Set a animation
    ActivityOptionsCompat options
            = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair1, pair2, pair3);

    intent.putExtra("model", post[0]);
    v.getContext().startActivity(intent, options.toBundle());
    ```

- ### Play music simultaneously
  ```java
  // 1. Create a music player for the number of tracks
  // 2. Prepare a music
  // 3. Register music player list 
  // 4. If all tracks are ready, change Flag to true
  public void setMusic(List<String> urls, ICallback callback) {
      for (String url : urls) {
          new Thread(() -> {
              MediaPlayer track = new MediaPlayer();
              track.setAudioStreamType(AudioManager.STREAM_MUSIC);
              try {
                  track.setDataSource(url);
                  track.prepare();
                  playerList.add(track);
                  countOfsession.set(countOfsession.get()+1);

                  if (countOfsession.get() == urls.size()) {
                      isPreparePlayers = true;
                      if (callback != null)
                          callback.initData(Const.RESULT_SUCCESS, "Sucess", null);
                  }
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }).start();
      }
  }
  ```
  
- ### Music Mixing (Didn't be applied on the project)
  ```Java
  // 1. Preparing music sources for mixing
  // 2. Check frequency and bit rate and set same
  // 3. Set array type according to bit rate (byte, short, etc.)
  // 4. Mxing
  // Note that because LittleEndian handles binary in Android, it is treated as LittleEndian
  private void mixSound() throws IOException {
      // Prepare Target music to combine
      InputStream in1=getResources().openRawResource(R.raw.test1_heeju);
      InputStream in2=getResources().openRawResource(R.raw.test2_heeju);        

      // Proces first music
      byte[] music1 = null;
      music1= new byte[in1.available()];
      music1= convertStreamToByteArray(in1);
      in1.close();
      short[] shortArrayTarget1 = byteToShortArray(music1, true);

      // Proces second music
      byte[] music2 = null;
      music2= new byte[in2.available()];
      music2= convertStreamToByteArray(in2);
      in2.close();
      short[] shortArrayTarget2 = byteToShortArray(music2, true);

      // Set the arrary for mixed music processing
      short[] output = new short[shortArrayTarget1.length];
      for (int i=0; i<output.length; i=i+1) {
          // Change the music file -1 to <samplef <1
          float samplef1 = shortArrayTarget1[i] / 32768.0f;
          float samplef2 = shortArrayTarget2[i] / 32768.0f;

          // Mix the sampled music file
          float mixed = samplef1 + samplef2;

          // Adjust volume, maximum and minimum
          mixed *= 0.8;
          if (mixed > 1.0f) mixed = 1.0f;
          if (mixed < -1.0f) mixed = -1.0f;

          // Sampling the music file
          short outputSample = (short)(mixed * 32768.0f);
          output[i] = outputSample;
      }

      // Set sampling frequency, bit rate, etc
      AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                                             AudioFormat.CHANNEL_OUT_MONO,
                                             AudioFormat.ENCODING_PCM_16BIT,
                                             music1.length,
                                             AudioTrack.MODE_STREAM);

      audioTrack.write(output, 0, output.length);

      // Play the combined music
      audioTrack.play();
  }
  ```

### 고은민

1. CustomView(EditText를 이용한 커스텀 뷰)

```Java
public class ClearEditView extends AppCompatEditText implements TextWatcher, View.OnTouchListener, View.OnFocusChangeListener{

    private Drawable clearDrawable;
    private OnTouchListener onTouchListener;
    private OnFocusChangeListener onFocusChangeListener;

    public ClearEditView(Context context) {
        super(context);
        init();
    }

    public ClearEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Drawable temp = ContextCompat.getDrawable(getContext(), R.drawable.ximage);
        clearDrawable = DrawableCompat.wrap(temp);
        DrawableCompat.setTintList(clearDrawable, getHintTextColors());
        clearDrawable.setBounds(0, 0, 30, 30);
        setClearIconVisible(false);

        addTextChangedListener(this);
        super.setOnFocusChangeListener(this);
        super.setOnTouchListener(this);
    }

    private void setClearIconVisible(boolean visible) {
        clearDrawable.setVisible(visible, false);
        setCompoundDrawables(null, null, visible ? clearDrawable : null, null);
    }



    //  ======================================================================================================TouchListener
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        if(clearDrawable.isVisible() && x > getWidth() - getPaddingRight() - 30) {
            if(event.getAction() == MotionEvent.ACTION_UP) {
                setText(null);
            }
            return true;
        }

        if (onTouchListener != null) {
            return onTouchListener.onTouch(v, event);
        } else {
            return false;
        }
    }

    @Override
    public void onFocusChange(final View view, final boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }

        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }

    @Override
    public void setOnFocusChangeListener( OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }



    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }


    //  ========================================================================================================= TextWatcher

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(isFocused()) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
```

2. Room - 구글 안드로이드 아키텍쳐 컴포넌트

> 참고 : [Room Persistence Library](https://developer.android.com/topic/libraries/architecture/room.html)

2-1 Room ?!

> - Room은 Sqlite 위에 만든 구글의 새로운 ORM 이다.   
> - Room은 개발자가 자료를 질의하기 위한 SQL을 작성해야 한다.  
> - 반한된 객체의 자식에 대한 게으른 로딩(lazy loading)을 지원하지 않는다.  
>   > lazy loading이란 : 객체가 필요한 시점까지 객체 초기화를 연기하기 위해 컴퓨터 프로그래밍에서 일반적으로 사용되는 디자인 패턴이다.  
>   >  
>   > 예를 들면 이미지 리스트를 출력하는 경우 ,  
>   > 사용자 브라우져에 보이는 이미지만 로딩하고 다른 이미지들은 사용자가 스크롤 하면서 이미지에 가까워지면 로딩된다.  
>   > [참고 1](https://code.i-harness.com/ko/q/8db2) , [참고 2](https://medium.com/@devkuu/lazy-loading-%EC%9D%B4%EB%9E%80-834be8c85833)

2-2 사용 방법 
> - 첫번쨰 gradle 설정
>   > - project수준의 build.gradle 설정
>   > ```gradle
>   > allprojects {
>   >     repositories {
>   >         jcenter()
>   >         maven { url 'https://maven.google.com' }    // 이부분
>   >     }
>   > }
>   > ```
>   > - app수준의 build.gradle 설정
>   > ```gradle
>   > implementation "android.arch.persistence.room:runtime:1.0.0"
>   > annotationProcessor "android.arch.persistence.room:compiler:1.0.0"
>   > ```

> - 코드 작성
>   > - 첫번째 : 테이블 정의
>   > ```java
>   > @Entity(tableName = "search")
>   > public class Search {
>   >     @PrimaryKey(autoGenerate = true)
>   >     private int id;
>   >     @ColumnInfo
>   >     private String search;
>   >     public Search() {}
>   >     public Search(String search) {
>   >         this.search = search;
>   >     }
>   >     public int getId() {
>   >         return id;
>   >     }
>   >     public void setId(int id) {
>   >         this.id = id;
>   >     }
>   >     public String getSearch() {
>   >         return search;
>   >     }
>   >    public void setSearch(String search) {
>   >         this.search = search;
>   >     }
>   > }
>   > ```
>   > 
>   > - 두번쨰 : DAO파일 작성
>   > ```java
>   > @Dao
>   > public interface SearchDao {
>   >     @Query("select distinct id, search from Search order by id desc")
>   >     List<Search> getAll();
>   >     @Insert
>   >     void insertItem(Search model);
>   > 
>   >     @Delete
>   >     void deleteItem(Search model);
>   >     @Query("delete from Search WHERE id NOT IN (SELECT MAX(id) FROM Search GROUP BY search)")
>   >     void deleteItems();
>   > }
>   > ```
>   >
>   > - 세번째 : Database생성
>   > ```java
>   > @Database(entities = {Search.class}, version = 1, exportSchema = false)
>   > public abstract class DBHelper extends RoomDatabase {
>   >     private static DBHelper INSTANCE = null;
>   >     private static final Object sLock = new Object();
>   >     public static DBHelper getInstance(Context context) {
>   >         synchronized (sLock) {
>   >             if (INSTANCE == null) {
>   >                 INSTANCE = Room
>   >                         .databaseBuilder(context.getApplicationContext()
>   >                                 , DBHelper.class
>   >                                 , "soundhub.db")
>   >                         .allowMainThreadQueries()
>   >                         .build();
>   >             }
>   >             return INSTANCE;
>   >         }
>   >     }
>   >     // Dao 선언
>   >     public abstract SearchDao searchDao();
>   > }
>   > ```
>   >
>   > - 네번쨰 : 사용
>   > ```java
>   > DBHelper dbHelper =  DBHelper.getInstance(view.getContext());
>   > SearchDao dao = dbHelper.searchDao();
>   > List<Search> data = dao.getAll();
>   > ```
