import bg.sofia.uni.fmi.mjt.smartcity.device.SmartCamera;
import bg.sofia.uni.fmi.mjt.smartcity.device.SmartDevice;
import bg.sofia.uni.fmi.mjt.smartcity.device.SmartLamp;
import bg.sofia.uni.fmi.mjt.smartcity.device.SmartTrafficLight;
import bg.sofia.uni.fmi.mjt.smartcity.hub.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.smartcity.hub.SmartCityHub;

import java.time.LocalDateTime;
import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        SmartDevice camera1 = new SmartCamera("cam1", 1.2, LocalDateTime.now().minusHours(4));
        SmartDevice camera2 = new SmartCamera("cam2", 1.2, LocalDateTime.now().minusHours(10));
        SmartDevice lamp1 = new SmartLamp("lamp1", 0.1, LocalDateTime.now().minusHours(5));
        SmartDevice trafficLighter1 = new SmartTrafficLight("lighter1", 0.5, LocalDateTime.now().minusDays(5));

        SmartCityHub cityHub = new SmartCityHub();
        try {
            cityHub.register(camera1);
            cityHub.register(camera2);
            cityHub.register(lamp1);
            cityHub.register(trafficLighter1);
        } catch (DeviceAlreadyRegisteredException e) {
            System.out.println(e.getMessage());
        }

        Collection<String> biggestConsumers = cityHub.getTopNDevicesByPowerConsumption(2);
        for (String id : biggestConsumers) {
            System.out.println(id);
        }
    }
}
