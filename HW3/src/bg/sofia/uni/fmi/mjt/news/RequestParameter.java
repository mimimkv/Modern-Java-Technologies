package bg.sofia.uni.fmi.mjt.news;

public class RequestParameter {

    // required parameter
    private final String[] keywords;

    // optional parameters
    private final String country;
    private final String category;

    public String[] getKeywords() {
        return keywords;
    }

    public String getCountry() {
        return country;
    }

    public String getCategory() {
        return category;
    }

    public static RequestParameterBuilder builder(String[] keywords) {
        return new RequestParameterBuilder(keywords);
    }

    private RequestParameter(RequestParameterBuilder requestParam) {
        this.keywords = requestParam.keywords;
        this.country = requestParam.country;
        this.category = requestParam.category;
    }

    public static class RequestParameterBuilder {
        private final String[] keywords;

        private String country;
        private String category;

        public RequestParameterBuilder(String[] keywords) {
            this.keywords = keywords;
        }

        public RequestParameterBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public RequestParameterBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public RequestParameter build() {
            return new RequestParameter(this);
        }
    }
}
