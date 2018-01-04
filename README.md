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
