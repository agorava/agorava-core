/*
 * Copyright 2013 Agorava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 */
package org.agorava.oauth;

/**
 * @author Antoine Sabot-Durand
 */
public class TestURLUtilsTest {
/*

    public static class PojoTest {
        public String param1;

        public Integer param2;

        public List<Double> param3;

        public String getParam1() {
            return param1;
        }

        public void setParam1(String param1) {
            this.param1 = param1;
        }

        public Integer getParam2() {
            return param2;
        }

        public void setParam2(Integer param2) {
            this.param2 = param2;
        }

        public List<Double> getParam3() {
            return param3;
        }

        public void setParam3(List<Double> param3) {
            this.param3 = param3;
        }

    }

    public static class PojoTest2 {
        private String param1;

        private Integer param2;

        private Map<String, Double> param3;

        public String getParam1() {
            return param1;
        }

        public void setParam1(String param1) {
            this.param1 = param1;
        }

        public Integer getParam2() {
            return param2;
        }

        public void setParam2(Integer param2) {
            this.param2 = param2;
        }

        */
/**
 * @return the param3
 *//*

        public Map<String, Double> getParam3() {
            return param3;
        }

        */
/**
 * @param param3 the param3 to set
 *//*

        public void setParam3(Map<String, Double> param3) {
            this.param3 = param3;
        }

    }

    URLUtils urlUtils = new URLUtilsImpl();

    */
/**
 * Test method for {@link org.agorava.utils.URLUtilsImpl#formURLEncodeMap(java.util.Map)}.
 *//*

    @Test
    public final void testFormURLEncodePojo() {
        PojoTest pojo = new PojoTest();
        List<Double> forParam3 = new ArrayList<Double>();
        forParam3.add(6789d);
        forParam3.add(90876d);

        pojo.setParam1("ézaè");
        pojo.setParam2(1234);
        pojo.setParam3(forParam3);

        String res = urlUtils.buildUri("http://service.com/network/updates?format=json", pojo);
        System.out.println(res);

        Assert.assertEquals(res,
                "http://service.com/network/updates?format=json&param1=%C3%A9za%C3%A8&param2=1234&param3=6789.0&param3=90876" +
                        ".0");
    }

    @Test(expected = AgoravaException.class)
    public final void testFormURLEncodePojoWithBadPojo() {
        PojoTest2 pojo = new PojoTest2();
        Map<String, Double> forParam3 = new HashMap<String, Double>();
        forParam3.put("1", 6789d);
        forParam3.put("2", 90876d);

        pojo.setParam1("ézaè");
        pojo.setParam2(1234);
        pojo.setParam3(forParam3);

        String res = urlUtils.buildUri("http://myurl.com", pojo);
        System.out.println(res);

        Assert.assertEquals(res, "http://myurl.com?param1=%C3%A9za%C3%A8&param2=1234&param3=6789.0&param3=90876.0");
    }
*/

}
