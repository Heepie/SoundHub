# SoundHub
- 프로젝트 소개
  1. GitHub처럼 각각 음악 파일을 합쳐, 더 좋은 음악을 만드는 플랫폼
  2. 기간: Nov.27, 2017 ~ Dec.31, 2017 (한달)
- 프로젝트의 목적
  1. MVVM 패턴, Data Binding 개념 이해 및 적용
  2. Git Flow 개념 이해 및 적용
  3. 클라이언트의 UI/UX를 결정을 협업하며 UI/UX 이해

- 프로젝트 관리
  1. Google Doc로 매주 해야할 일 작성 (http://bit.ly/2E6to0D)
  2. Hangout를 통한 데일리 스크럽
  
- 프로젝트 시연
  1. 동영상: https://www.youtube.com/watch?v=toBCfZmIJk0&t=7s

## 사용한 아키텍처(MVVM)
- 아키텍처를 사용한 이유
  1. 코드 수정에 따른 스파게티 코드 문제점 직면
- MVVM을 선택한 이유
  1. MVVM은 View와 ViewModel N:1 관계(테스트 수월)
  2. MVVM, MVP 경험 필요

## 스크린 샷
- 로그인 (일반회원가입, 소셜회원가입, 두번 클릭 후 종료)
![login](https://github.com/Heepie/SoundHub/blob/develop/img/screen_shot1.png)
- Home (Navigation View, 다중 리사이클러 뷰)
![home](https://github.com/Heepie/SoundHub/blob/develop/img/screen_shot2.png)
- Detail Post (음악 동지 재생, 음악 Mixing 요청, 녹음 업로드, 파일 업로드)
![detail](https://github.com/Heepie/SoundHub/blob/develop/img/screen_shot3.png)

## 주요 코드
### 문희주
- ### MVVM, Data Binding
![flow](https://github.com/Heepie/SoundHub/blob/develop/img/mvvm_flow_watermark.png)</br> 
  1. DataAPI와 Server 통신
  ```Java
  public Observable<Response<Data>> getData(String category) {
      // Retrofit 설정
      IData service = retrofit.create(IData.class);

      // RESTFul에 따라 서버와 통신
      if (category == Const.CATEGORY_DEFAULT)
          return service.getData("");

      return service.getData(category);
  }
  ```
  2. DataAPI와 ViewModel 통신
  ```Java
  public void setDisplayData(String category, ICallback callback) {
      dataAPI = DataAPI.getInstance();
      Observable<Response<Data>> dataObs = dataAPI.getData(category);

      // RxJava를 통한 Thread 설정
      dataObs.subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(
                  jsonData -> {
                  // 네트워크로 입력 받은 데이터 DataModel에 셋팅
                  dataAPI.setModelData(jsonData.body());
                  data = jsonData.body();
                  setData(data);
                  callback.initData(Const.RESULT_SUCCESS, "Success", null);
              });
  }
  ```
  3. View와 ViewModel 연결
  ```Java
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      // Binding 초기화
      listBinding = DataBindingUtil.setContentView(this, R.layout.list_view);
      listViewModel = new ListViewModel(this);

      // ViewModel Binding
      listBinding.setVariable(BR.viewModel, listViewModel);
      listBinding.setVariable(BR.view, this);
  }

  private void initData(String category, ICallback callback) {
      listBinding.progressBar.setVisibility(View.VISIBLE);
      listViewModel.resetData();

      // ViewModel의 비지니스 로직 호출
      listViewModel.setDisplayData(category, callback);
      listViewModel.setDisplayCategory();
  }
  ```
  4. View 설정
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
        <!-- ViewModel 변수 설정 -->
        <variable
            name="viewModel"
            type="com.heepie.soundhub.viewmodel.ListViewModel"/>
    </data>

    <!-- BindingAdapter를 통한 Data Binding -->
    <android.support.v7.widget.RecyclerView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="@color/black"
        app:activity="@{view}"
        app:setOuterViewModel="@{viewModel}">
  </layout>
  ```
- ### 머티리얼 디자인 (Material Design)
    1. XML
    ```XML
    <!-- 공유하는 View에 transitionName 설정 -->
    <ImageButton
        android:src="@drawable/comments"
        android:transitionName="@string/sharedComment"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@+id/list_comment_count"
        app:layout_constraintTop_toTopOf="parent"/>
    ```
    2. Java
    ```Java
    // 공유하는 View와 transitionName 연결
    Pair<View, String> pair1 = Pair.create(image, image.getTransitionName());
    Pair<View, String> pair2 = Pair.create(like, like.getTransitionName());
    Pair<View, String> pair3 = Pair.create(comment, comment.getTransitionName());

    // 애니메이션 설정
    ActivityOptionsCompat options
            = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pair1, pair2, pair3);

    // 인텐트를 통한 Activity 호출
    intent.putExtra("model", post[0]);
    v.getContext().startActivity(intent, options.toBundle());
    ```

- ### 음악 동시 재생
  ```java
  // 1. 음악 플레이어를 track 개수만큼 생성
  // 2. 음악 Prepare
  // 3. 음악 플레이어 리스트에 등록
  // 4. 모든 track이 준비되면 Flag를 true로 변경
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
  
- ### 음악 Mixing (프로젝트에 적용되지는 않음)
  ```Java
  // 1. Mixing할 음원 준비
  // 2. 주파수 및 bit rate 확인 및 동일한 설정
  // 3. bit rate에 따라 배열 자료형 설정 (byte, short 등)
  // 4. Mxing
  // 주의 할 점은 Android에서 binary를 LittleEndian처리 하기 때문에 LittleEndian으로 처리
  private void mixSound() throws IOException {
      // 합치기 원하는 Target 음악 준비
      InputStream in1=getResources().openRawResource(R.raw.test1_heeju);
      InputStream in2=getResources().openRawResource(R.raw.test2_heeju);        

      // 첫번째 음악 가공
      byte[] music1 = null;
      music1= new byte[in1.available()];
      music1= convertStreamToByteArray(in1);
      in1.close();
      short[] shortArrayTarget1 = byteToShortArray(music1, true);

      // 두번째 음악 Byte로 가공
      byte[] music2 = null;
      music2= new byte[in2.available()];
      music2= convertStreamToByteArray(in2);
      in2.close();
      short[] shortArrayTarget2 = byteToShortArray(music2, true);

      // Mixing한 음악처리를 위한 배열선언
      short[] output = new short[shortArrayTarget1.length];
      for (int i=0; i<output.length; i=i+1) {
          // 해당 음악 파일 -1 < samplef < 1로 변경
          float samplef1 = shortArrayTarget1[i] / 32768.0f;
          float samplef2 = shortArrayTarget2[i] / 32768.0f;

          // 샘플링한 음악 파일 Mixing
          float mixed = samplef1 + samplef2;

          // 볼륨 조절 및 최대, 최소값 조절
          mixed *= 0.8;
          if (mixed > 1.0f) mixed = 1.0f;
          if (mixed < -1.0f) mixed = -1.0f;

          // 음악 파일 샘플링
          short outputSample = (short)(mixed * 32768.0f);
          output[i] = outputSample;
      }

      // 샘플링 주파수, bit rate 등의 기준 설정
      AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                                             AudioFormat.CHANNEL_OUT_MONO,
                                             AudioFormat.ENCODING_PCM_16BIT,
                                             music1.length,
                                             AudioTrack.MODE_STREAM);

      audioTrack.write(output, 0, output.length);

      // 해당 음원 플레이
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