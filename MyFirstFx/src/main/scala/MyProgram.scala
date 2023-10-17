import scalafx.animation.{FadeTransition, TranslateTransition}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.{Group, Scene}
import scalafx.scene.effect.DropShadow
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, Text}
import scalafx.util.Duration

object MyProgram extends JFXApp
{
  stage = new PrimaryStage()
  {
    title = "My First Fx"
    width = 400
    height = 400
    scene = new Scene()
    {
      val myFont = new Font("Cascadia Mono", 20)
      val image = getClass().getResourceAsStream("Untitled.jpeg")
      val img = new Image(image)
      content = List(
        new Group
        (
          new Text(600,30, "Doggy Style")
          {
            font = myFont
            effect = new DropShadow(10, 10, 10, Color.Orange)
          },
          new Text(600,600, "Style Doggy")
          {
            font = myFont
            effect = new DropShadow(10, 10, 10, Color.Red)
          }
        ),
        new ImageView(img)
        {
          x = 300
          y = 350
          fitWidth = 200
          fitHeight = 200
        }
      )
      import scalafx.Includes._ //auto convert from javaFx to ScalaFx
      val tt = new TranslateTransition(Duration(2000), content.get(1))
      {
        byX = 200
        cycleCount = TranslateTransition.Indefinite
        autoReverse = true
      }
      tt.play()
      val ft = new FadeTransition(Duration(500), content.get(1))
      {
        fromValue = 1
        toValue = 4
        cycleCount = FadeTransition.Indefinite
        autoReverse = true
      }
      ft.play()
    }
  }
}
