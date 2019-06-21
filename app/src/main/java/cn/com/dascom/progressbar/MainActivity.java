package cn.com.dascom.progressbar;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar1;
    float i =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar1 = findViewById(R.id.progressBar2);
    }

    public void Button(View view) {

        /**
         * Android已经实现了这种线程间的通信, 这个类就是AsyncTask
         * 第一个泛型:   子线程执行任务的请求参数类型
         * 第二个泛型:   子线程执行任务的进度
         * 第三个泛型:   子线程执行任务的结果返回类型
         */
        new AsyncTask<Integer, Integer, Integer>() {

            /**
             * 在AsyncTask的doInBackground方法中执行后台任务
             * 运行在子线程中,执行耗时的操作
             * 当doInBackground方法执行完毕后，会触发onPostExecute方法
             * （该方法在主线程中进行，可以在该方法中更新界面组件）
             * @param integers
             * @return
             */
            @Override
            protected Integer doInBackground(Integer... integers) {
                for (int i = 1; i <= 10000; i++) {
                    doSomethingTask(i);
                }
                return 10000;
            }

            private void doSomethingTask(int num) {
                try {
                    Thread.sleep(1);
//                    而执行publishProgress方法，会触发onProgressUpdate方法
//                    （该方法在主线程中进行，可以在该方法中更新界面组件，
//                    这里更新的是进度条的进度）
                    publishProgress(num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /**
             *运行在主线程中, 负责初始化的工作(进度条对话框的初始)
             */
            @Override
            protected void onPreExecute() {
            }

            /**
             *运行在主线程中, 更新UI
             * @param result
             */
            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
            }

            /**
             * 运行在主线程中, 实时更更新界面组件
             * @param values
             */
            @Override
            protected void onProgressUpdate(Integer... values) {
                setElectircProgress(values[0],0xff666666);
                super.onProgressUpdate(values);
            }

        }
        .execute(1);//启动异步任务(必须在主线程中启动)
        //在主线程中取消异步任务 myTask.cancel(true);
        //每个AsyncTask 的实例只能被使用一次, 不能重复使用,否则会抛出异常
//        int color = getCurrentColor(i,0x000000,0xffffff);
//        Log.d("TAG", "Button: "+color);
//        i += 0.1;

    }

    private void setElectircProgress(int i, int color) {
        ClipDrawable drawable = new ClipDrawable(new ColorDrawable(color)
                , Gravity.LEFT, ClipDrawable.HORIZONTAL);
        progressBar1.setProgressDrawable(drawable);
        progressBar1.setProgress(i);

    }

    /**
     * 根据fraction值来计算当前的颜色。
     */
    private int getCurrentColor(float fraction, int startColor, int endColor) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        redCurrent = (int) (redStart + fraction * redDifference);
        blueCurrent = (int) (blueStart + fraction * blueDifference);
        greenCurrent = (int) (greenStart + fraction * greenDifference);
        alphaCurrent = (int) (alphaStart + fraction * alphaDifference);

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }
}
