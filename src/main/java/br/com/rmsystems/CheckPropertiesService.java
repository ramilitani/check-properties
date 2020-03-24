package br.com.rmsystems;

import br.com.rmsystems.model.KeyValue;
import br.com.rmsystems.utils.Constants;
import br.com.rmsystems.utils.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CheckPropertiesService {

    private List<KeyValue> templateProperties;
    boolean isError = false;

    public void checkConformity()
    {
        File templateFile = FileUtils.loadResource(Constants.TEMPLATE_RESOURCE);
        File tobecheckedFile = FileUtils.loadResource(Constants.TOBECHECKED_RESOURCE);
        List<KeyValue> templateProperties = readProperties(templateFile);
        List<KeyValue> toBeCheckedProperties = readProperties(tobecheckedFile);
        verifyIfPropertyExist(templateProperties, toBeCheckedProperties);
        logCharsetCodeList(toBeCheckedProperties);

        if (isError) {
            System.out.println("There are errors to analyse in the log file");
        }
    }

    private List<KeyValue> readProperties(File file)
    {
        List<KeyValue> properties = new ArrayList<>();
        int index = 0;
        FileUtils.cleanLogFile(Constants.LOG_FILE);
        File logFile = FileUtils.loadResource(Constants.LOG_FILE);

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while((line = br.readLine()) != null) {
                index++;
                try {
                    if (!line.startsWith("#") && !line.isEmpty()) {
                        String[] keyValue = line.split("=");
                        String value = "";
                        if (keyValue.length == 2) {
                            value = keyValue[1];
                        }
                        KeyValue property = new KeyValue(keyValue[0].trim(),  value);
                        properties.add(property);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isError = true;
                    FileUtils.writeInFile(logFile,"Error in line: " + index + " from file: " + file.getName());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error in line: " + index + " from file: " + file.getName());
        }

        return properties;
    }

    private void logCharsetCodeList(List<KeyValue> keyValues) {

        Set<String> charsetCodes = keyValues.stream()
                .filter(keyValue -> {
                    return keyValue.getValue().indexOf("\\u") != -1;
                })
                .map(keyValue -> {
                    int index = keyValue.getValue().indexOf("\\u");
                    String charset = keyValue.getValue().substring(index, index+6);
                    return charset;
                }).collect(Collectors.toSet());

        if (charsetCodes != null && !charsetCodes.isEmpty()) {
            logCharsetCodes(charsetCodes);
        }
    }

    private void verifyIfPropertyExist(List<KeyValue> templateProperties, List<KeyValue> toBeCheckedProperties)
    {
        List<String> missingProperties = new ArrayList<>();
        for (int i = 0; i < templateProperties.size(); i++) {
            KeyValue keyValueTemplate = templateProperties.get(i);
            boolean exist = false;
            for (int j = 0; j < toBeCheckedProperties.size(); j++) {
                KeyValue keyValueToBeChecked = toBeCheckedProperties.get(j);
                if (keyValueTemplate.getKey().equals(keyValueToBeChecked.getKey())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                missingProperties.add(keyValueTemplate.getKey());
            }
        }

        if (!missingProperties.isEmpty()) {
            isError = true;
            printMissingProperties(missingProperties);
        }
    }

    private void printMissingProperties(List<String> missingProperties)
    {
        File logFile = FileUtils.loadResource(Constants.LOG_FILE);
        FileUtils.writeInFile(logFile, "There are missing properties: ");
        StringBuffer stringBuffer = new StringBuffer();
        for (String property : missingProperties) {
            stringBuffer.append("\n" + property + ",\n");
        }
        FileUtils.writeInFile(logFile, stringBuffer.toString());
    }

    private void logCharsetCodes(Set<String> charsetCodes)
    {
        File charsetLogFile = FileUtils.loadResource(Constants.CHARSET_LOG_FILE);
        FileUtils.cleanLogFile(Constants.CHARSET_LOG_FILE);
        FileUtils.writeInFile(charsetLogFile, "Charset into the file: ");
        StringBuffer stringBuffer = new StringBuffer();
        for (String charset : charsetCodes) {
            stringBuffer.append("\n" + charset + ",\n");
        }
        FileUtils.writeInFile(charsetLogFile, stringBuffer.toString());
    }
}
