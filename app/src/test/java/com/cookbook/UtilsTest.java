package com.cookbook;


import com.cookbook.helpers.StringsUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilsTest {

    @Test
    public void capitalizeTest() {
        String testString = "название рецепта";
        String expected = "Название рецепта";
        String capitalizedString = StringsUtils.capitalize(testString);
        assertEquals(expected,capitalizedString);
    }

}
