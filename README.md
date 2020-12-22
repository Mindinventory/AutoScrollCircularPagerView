# AutoScrollCircularPagerView
[![](https://jitpack.io/v/Mindinventory/AutoScrollCircularPagerView.svg)](https://jitpack.io/#Mindinventory/AutoScrollCircularPagerView)

AutoScrollCircularPagerView is a library to show slider with features of circular auto scroll with configurable    

![image](/media/auto-scroll-pager-view.gif)

### Key features

* Circular image slider
* Easy to customise auto scroll time interval  
* Configurable dots position and colors

# Usage

### Dependencies

* Step 1. Add the JitPack repository to your build file
    
    Add it in your root build.gradle at the end of repositories:

    ```groovy
	    allprojects {
		    repositories {
			    ...
			    maven { url 'https://jitpack.io' }
		    }
	    }
    ``` 

* Step 2. Add the dependency
    
    Add it in your app module build.gradle:
    
    ```groovy
        dependencies {
            ...
            implementation 'com.github.Mindinventory:AutoScrollCircularPagerView:0.0.1'
        }
    ``` 

### Implementation


* Step 1. Add AutoScrollCircularPagerView in to your layout class:
    
    ```xml
            <com.mindinventory.AutoScrollCircularPagerView
                    android:id="@+id/autoScrollContainer"
                    android:layout_width="0dp"
                    android:layout_height="200dp"
                    android:orientation="horizontal"
                    app:auto_scroll_delay="5000"
                    app:dot_margin="20dp"
                    app:dot_gravity="bottom"
                    app:layout_constraintLeft_toLeftOf="parent"
                    app:layout_constraintRight_toRightOf="parent"
                    app:layout_constraintTop_toTopOf="parent"
                    app:selected_dot_color="@color/selected_color"
                    app:unselected_dot_color="@color/unselected_color" />
    ```

* Step 2. Setup adapter and items for AutoScrollCircularPagerView in to your Activity or Fragment:


    ```kotlin
    class MainActivity : AppCompatActivity() {
        private val images = ArrayList<Int>().apply {
            add(R.drawable.image1)
            add(R.drawable.image2)
            add(R.drawable.image3)
            add(R.drawable.image4)
            add(R.drawable.image5)
        }
        private val myAdapter by lazy { MyAdapter() }
    
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            autoScrollContainer.setAdapter(myAdapter)
            autoScrollContainer.setItems(images)
        }
    }
    ```
---------------------------------------------------------------------
### Guideline for contributors
Contribution towards our repository is always welcome, we request contributors to create a pull request to the **develop** branch only.  

### Guideline to report an issue/feature request
It would be great for us if the reporter can share the below things to understand the root cause of the issue.

* Library version
* Code snippet
* Logs if applicable
* Device specification like (Manufacturer, OS version, etc)
* Screenshot/video with steps to reproduce the issue

### Requirements

* minSdkVersion >= 23
* Androidx

# LICENSE!

AutoScrollCircularPagerView is [MIT-licensed](/LICENSE).

# Let us know!
We’d be really happy if you send us links to your projects where you use our component. Just send an email to sales@mindinventory.com And do let us know if you have any questions or suggestion regarding our work.
