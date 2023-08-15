# VIEW BINDING IN FRAGMENT

___

## CÁCH SỬ DỤNG VIEW BINDING LIÊN KẾT UI COMPONENT TRONG LAYOUT VỚI DATA SOURCE TRONG APP

- thông thường để làm việc với các __View__ trên __Layout__ ta cần xác định thuộc tính __id__ thông qua method __findViewById()__, với cách này
    - quá trình xây dựng ứng dụng tốn thời gian
    - không được kiểm tra tại thời điểm biên dịch, nên khi xác định sai __id__ sẽ dẫn đến ứng dụng bị crash
- để khắc phục những nhược điểm trên, Google cung cấp giải pháp __View Binding__

- ví dụ ta có file layout __activity_main.xml__ như sau
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center_horizontal"
    android:orientation="vertical"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/btn_click"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:text="@string/open_fragment" />

    <FrameLayout
        android:id="@+id/content_layout"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

___

### CÁC BƯỚC THIẾT LẬP VÀ SỬ DỤNG VIEW BINDING

- Khai báo sử dụng __View Binding__ trong __build.gradle module:app__, trong thẻ __android__ ta thêm vào đoạn code bật __viewBinding__ là __true__, sau đó click __Sync Now__
```js
android {
    // 

    buildFeatures {
        viewBinding true
    }
}
```

> nếu muốn 1 __layout__ nào đó bỏ qua quá trình tạo class Binding (không sử dụng View Binding) ta thêm thuộc tính sau vào __root view__ của __layout__ đó
>> <LinearLayout
>>        ...
>>       tools:viewBindingIgnore="true" >
>>    ...
>> </LinearLayout>

- sau khi bật được __viewBinding__ thì __Android__ sẽ tự sinh ra các file class Binding cho các __layout__ có trong ứng dụng
- ví dụ: file __layout__ __activity_main.xml__ thì sẽ có file Binding là __ActivityMainBinding.java__
- ta sẽ khai báo đối tượng __Binding__ này trong file java tương ứng với layout
- __MainActivity.java__
```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // nơi khởi tạo đối tượng Binding
        setContentView(R.layout.activity_main);
}
```

> Lưu ý: TA CÓ THỂ KHÔNG CẦN KHAI BÁO BIỂN ĐỂ QUẢN LÝ LIÊN KẾT __id__ các View trên Layout

- sau khi khai báo, ta khởi tạo đối tượng Binding sau ``super.onCreate(savedInstanceState);`` đồng thời chỉnh sửa lại method ``setContentView()``
- __MainActivity.java__
```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView( mActivityMainBinding.getRoot());

        // hoặc có thể viết tách riêng đối tượng View dùng để set content cho giao diện
//        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = mActivityMainBinding.getRoot();
//        setContentView(view);        
}
```

- sử dụng đối tượng __Binding__ đã khởi tạo để gọi đến __id__ của các __View__ trên __Layout__ và có thể thực hiện thao tác trực tiếp các method
- __MainActivity.java__
```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mActivityMainBinding.getRoot();
        setContentView(view);
        
        mActivityMainBinding.btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "TEST.......................", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

- hoặc có thể khai báo đối tượng View sau đó sử dụng đối tượng Binding gọi đến id để gán cho đối tượng View để thao tác gọn hơn, nhưng cách này cũng gần giống như cách sử dụng __findViewById()__

> khi ta sử dụng View Binding thì class Binding của các Layout tương ứng sẽ được sinh ra
>> các class này sẽ quản lý __id__ của các view trong layout tương ứng.
>> khi sử dụng đối tượng Binding, chỉ cần nhập dấu ``.`` phía sau đối tượng đó, thì danh sách id mà nó quản lý sẽ được xổ xuống cho ta chọn.
>> về quy tắc đặt id cho View trên Layout.
>>> phân biệt UPPERCASE và lowercase.
>>> dấu gạch nối ``_`` sẽ bị loại bỏ, những ký tự đầu sau dấu ``_`` sẽ được viết UPPERCASE.
>>> vì vậy tránh trường hợp khi sau khi bỏ đi dấu ``_`` ta có 2 id giống nhau trên layout, lúc này đối tượng Binding sẽ liên kết bị lỗi, xử lý logic sẽ lẫn lộn

___

### BẮT ĐẦU VỚI FRAGMENT

- sau khi cài đặt View Binding, khởi tạo và sử dụng View Binding gọi đến View trong Layout, ở __MainActivity.java__ thông qua __AcitivtyMainBinding__ gọi đến đối View Button xử lý sự kiện người dùng click vào, ta tiến hành xây dựng hàm gọi replace Fragment lên FrameLayout.
- trước đó ta cần tạo Fragment trong project
- __MyFragment.java__
```java
public class MyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }
}
```
- __fragment_my.xml__
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/black"
    android:padding="14dp"
    tools:context=".MyFragment">

    <!-- TODO: Update blank fragment layout -->
    <TextView
        android:id="@+id/tv_name"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center_horizontal"
        android:text="@string/hello_my_fragment"
        android:textColor="@color/white"
        android:textSize="30sp" />

</FrameLayout>
```

- trong sự kiện click Button của MainActivity ta gọi đến method, method này sẽ thực hiện công việc replace Fragment lên FrameLayout
- __MainActivity.java__
```java
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mActivityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mActivityMainBinding.getRoot();
        setContentView(view);
        
        mActivityMainBinding.btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyFragment();
            }
        });
    }

    private void openMyFragment() {
        MyFragment myFragment = new MyFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_layout, myFragment);
        transaction.commitAllowingStateLoss();
    }
}
```

___

### VIEW BINDING VỚI FRAGMENT

- sau khi thực hiện các bước ở trên ta vẫn chưa sử dụng View Binding trên Fragment, ở đây có 1 đối tượng View trên Fragment là TextView, ta sẽ thực hiện View Binding với View này.
- tương tự như Activity có file layout đi kèm, Fragment cũng có file layout đi kèm, và khi ta bật View Binding trong ``build.gradle module:app`` thì 1 file class Binding với các layout có trong project sẽ được sinh ra, ở đây sau khi xây dựng được MyFragment thì ta sẽ có 1 class ``FragmentMyBinding`` tương ứng với layout ``fragment_my.xml`` để sử dụng cho việc Binding View trong __MyFragment__.
- cách sử dụng thì tương tự như trong __Activity__, chỉ khác là khi khởi tạo đối tượng ViewBinding sẽ khác đôi chút so với trong Activity
- ngoài ra trong Fragment đôi khi ta cần sử dụng đối tượng View của Fragment để thao tác, ta có thể khai báo đối tượng View toàn cục và khởi tạo đối tượng này để sử dụng cho toàn bộ Fragment
- __MyFragment.java__
```java
public class MyFragment extends Fragment {
    // đối tượng View Binding
    private FragmentMyBinding mFragmentMyBinding;

    // đối tượng instance View của Fragment
    private View mView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mFragmentMyBinding = FragmentMyBinding.inflate(inflater, container, false);

        mFragmentMyBinding.tvName.setText("TEST VIEW BINDING IN FRAGMENT");

        // khởi tạo đối tượng View và return về cho Fragment
        mView = mFragmentMyBinding.getRoot();
        return mView;
    }
}
```