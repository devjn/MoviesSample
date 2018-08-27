package com.github.devjn.moviessample.rules;

import com.github.devjn.moviessample.R;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.test.rule.ActivityTestRule;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;


/**
 * A rule to be used for testing of a single fragment. Fragment will be opened before each test
 * annotated with @Test and before methods annotated with @Before. During the duration of the test
 * you will be able to manipulate Fragment directly.
 *
 * @param <A> - the activity to test.
 */
class OpenFragmentRule<A extends AppCompatActivity> implements TestRule {

    private final ActivityTestRule<A> activityRule;
    private final Fragment fragment;
    private final int timeoutSec = 5;

    /**
     * Create fragment test rule, don't forget to add @Rule.
     *
     * @param activityRule - Activity Test Rule for starting fragment.
     * @param fragment - fragment for opening.
     */
    OpenFragmentRule(ActivityTestRule<A> activityRule, Fragment fragment) {
        this.activityRule = activityRule;
        this.fragment = fragment;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                openFragment(activityRule.getActivity(), fragment);
                await().atMost(timeoutSec, SECONDS).until(fragment::isResumed);
                statement.evaluate();
            }
        };
    }

    /**
     * Replace fragment in R.id.container from activity.
     *
     * @param activity - current AppCompatActivity.
     * @param newFragment - fragment that will replace old fragment.
     */
    public static void openFragment(AppCompatActivity activity, Fragment newFragment) {
        activity
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, newFragment)
                .commitAllowingStateLoss();
    }
}