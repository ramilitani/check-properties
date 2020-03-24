package br.com.rmsystems;

public class CheckPropertiesApp {

    public static void main (String[] args)
    {
        System.out.println("------ STARTING CHECK-PROPERTIES APPLICATION ------");
        CheckPropertiesService service = new CheckPropertiesService();
        service.checkConformity();
        System.out.println("PROCESSING DONE");
    }
}
