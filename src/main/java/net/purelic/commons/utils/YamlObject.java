package net.purelic.commons.utils;

import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class YamlObject<E extends Enum<E>> {

    private final Map<String, Object> yaml;

    public YamlObject(Map<String, Object> yaml) {
        this.yaml = yaml;
    }

    public Map<String, Object> getYaml() {
        return this.yaml;
    }

    public <T extends Enum<T>> T get(E enumKey, T defaultValue) {
        String key = enumKey.name().toLowerCase();
        String defaultName = defaultValue.name();
        String value = (String) this.yaml.getOrDefault(key, defaultName);

        try {
            return T.valueOf(defaultValue.getDeclaringClass(), value.toUpperCase());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public <T> T get(E enumKey, T defaultValue) {
        return (T) this.yaml.getOrDefault(enumKey.name().toLowerCase(), defaultValue);
    }

    public <T> T get(E enumKey) {
        return (T) this.yaml.get(enumKey.name().toLowerCase());
    }

    public <T> T get(String key) {
        return (T) this.yaml.get(key);
    }

    public <T> T get(String key, T defaultValue) {
        return (T) this.yaml.getOrDefault(key, defaultValue);
    }

}

