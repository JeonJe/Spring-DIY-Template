package stduy.reflection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        logger.info(carClass::getName);
    }

    /**
     * stduy.reflection.ReflectionTest testMethodRun
     * INFO: test : null
     * stduy.reflection.ReflectionTest testMethodRun
     * INFO: test : 0
     */
    @Test
    @DisplayName("test로 시작하는 메소드 실행")
    void testMethodRun() {
        Class<Car> carClass = Car.class;
        Method[] declaredMethods = carClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().startsWith("test")) {
                //private 실행
//                declaredMethod.setAccessible(true);
                try {
                    Object invoke = declaredMethod.invoke(new Car());
                    logger.info(() -> String.valueOf(invoke));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 자동차 정보를 출력 합니다.
     */
    @Test
    @DisplayName("PrintView 애노테이션이 붙은 메소드 실행")
    void testAnnotationMethodRun() {
        Class<Car> carClass = Car.class;
        Method[] declaredMethods = carClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(PrintView.class)) {
                try {
                    declaredMethod.invoke(new Car());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * INFO: test : 소나타
     */
    @Test
    @DisplayName("private field에 값 할당")
    void privateFieldAccess() throws Exception {
        Class<Car> carClass = Car.class;
        Field nameField = carClass.getDeclaredField("name");
        Car car = carClass.getDeclaredConstructor().newInstance();

        nameField.setAccessible(true);
        nameField.set(car, "소나타");

        logger.info(car::testGetName);

    }

    /**
     *  INFO: test : null
     *  INFO: test : 소나타
     *  INFO: test : 100
     */
    @Test
    @DisplayName("인자를 가진 생성자의 인스턴스")
    void constructorWithArgs() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<Car> clazz = Car.class;
        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor<?> declaredConstructor : declaredConstructors) {
            if (declaredConstructor.getParameterCount() == 0) {
                Car car = (Car) declaredConstructor.newInstance();
                logger.info(car::testGetName);
                continue;
            }
            if (declaredConstructor.getParameterCount() == 2) {
                Car car = (Car) declaredConstructor.newInstance("소나타", 100);
                logger.info(car::testGetName);
                logger.info(car::testGetPrice);
            }

        }
    }


}
