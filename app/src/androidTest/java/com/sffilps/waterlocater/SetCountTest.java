package com.sffilps.waterlocater;

import android.support.test.runner.AndroidJUnit4;

import com.sffilps.waterlocater.controllers.SubmitReport;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by ckramer on 4/5/17.
 */

@RunWith(AndroidJUnit4.class)
public class SetCountTest {

    /**
     * Tests to make sure the variable passed in, count,
     * is incremented by one
     */
    @Test
    public void incrementCount() {
        String i = "1";
        String count = SubmitReport.setCount(i);
        int countNum = Integer.parseInt(count);
        assertThat(countNum, is(2));
    }

    /**
     * Tests to make sure an exception is thrown if null is passed in.
     */
    @Test(expected = NullPointerException.class)
    public void testEmptyStrings() {
        String i = null;
        String count = SubmitReport.setCount(i);
    }


    /**
     * Test to make sure if anything but an integer is passed into the function, an error will be thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput() {
        String i = "a";
        String count = SubmitReport.setCount(i);
    }








}
