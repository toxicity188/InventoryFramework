# InventoryFramework
InventoryFramework는 고도의 인벤토리 제작을 위한 간단한 프레임워크입니다.

## 프레임워크에 플러그인 등록하기
``` java
import kr.toxicity.inventory.api.annotation.InventoryPlugin;

@InventoryPlugin
public class InventoryExample extends JavaPlugin {
}
```
InventoryPlugin 어노테이션을 자신의 메인 클래스에 붙입니다.  
directory를 지정하여 프레임워크를 사용할 폴더명을 지정해 줍니다. 기본 폴더명은 invenntoryframework입니다.
![스크린샷 2024-02-19 224004](https://github.com/toxicity188/InventoryFramework/assets/114675706/45917f1f-396f-4a13-b46b-e69c76a47b14)  
다음과 같이 지정한 이름의 폴더가 생겼다면 성공입니다.

## 인벤토리 배경 설정하기
``` java
import kr.toxicity.inventory.api.annotation.InventoryBackground;

@InventoryBackground(
        asset = "test.png"
)
public class Foo extends GuiAsset {
}
```
다음과 같이 GuiAsset을 구현한 클래스에 InventoryBackground 어노테이션을 붙입니다.  
그 다음 test.png에 해당하는 이미지를 자신의 프레임워크 폴더/Background 경로에 넣습니다.

## 애니메이션 등록하기
``` java
import kr.toxicity.inventory.api.annotation.InventoryAnimation;

@InventoryAnimation(
        asset = "test.png",
        playTime = 20,
        type = AnimationType.LOOP,
        xEquation = "10cos(t/10 * pi) + 20",
        yEquation = "10sin(t/10 * pi) - 10"
)
public class Foo extends GuiAsset {
}
```
다음과 같이 GuiAsset을 구현한 클래스에 InventoryAnimation 어노테이션을 붙입니다.    
그 다음 애니메이션이 플레이타임 별로 이동할 위치를 수식의 형태로 입력합니다.

## 텍스트 표시하기
``` java
@InventoryText(
        scale = 24,
        multiplier = 0.5,
        y = -10,
        asset = "test.ttf"
)
public class TestAsset implements GuiAsset, GuiText {
    @Override
    public @NotNull String text(@NotNull Player player) {
        return "Health : " + player.getHealth();
    }
}

```
다음과 같이 GuiAsset과 GuiText를 구현한 클래스에 InventoryText 어노테이션을 붙입니다.  
그 다음 test.ttf에 해당하는 폰트를 자신의 프레임워크 폴더/text 경로에 넣습니다.

## GUI 오픈하기
``` java
InventoryFramework.getInstance().builder()
    .append(new TestAsset())
    .append(new TestAsset2())
    .setExecutor(new GuiExecutor() {
        @Override
        public void init(@NotNull Player player, @NotNull GuiAnimation animation) {
            player.sendMessage("Hello world!");
        }

        @Override
        public void click(@NotNull Player player, @NotNull ClickData data, @NotNull GuiAnimation animation) {
            player.sendMessage("The clicked slot is " + data.clickedSlot() + "!");
            data.setCancelled(true);
            if (data.clickedSlot() == 0) {
                animation.toggle(TestAsset.class);
            }
        }

        @Override
        public void end(@NotNull Player player, @NotNull GuiAnimation animation) {
            player.sendMessage("Ended!");
        }
    })
    .build()
    .open(player);
```
InventoryFramework 객체에서 Builder를 받아옵니다.  
다음과 같은 형식으로 에셋을 등록하고 애니메이션을 제어합니다.

## 리소스팩 적용하기
``` yaml
    merge_other_plugins_resourcepacks_folders:
    - InventoryFramework/build
```
ItemsAdder 기준 다음과 같은 콘피그를 사용합니다.  
해당 경로의 내용물을 본인의 리소스팩과 합치십시오.
