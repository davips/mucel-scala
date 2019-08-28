package app

import java.awt.Color
import javax.swing.JFrame
import javax.swing.WindowConstants

import data.Cfg
import view.Animation

object Main extends App {
  def run() {
    val anima = Animation(1 / 20d)
    val frame = new JFrame()
    frame.getContentPane.add(anima)
    frame.setBackground(Color.gray)
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    frame.setSize(Cfg.frameWidth, Cfg.frameHeight)
    frame.setVisible(true)
    while (true) {
      anima.update()
      frame.repaint()
    }
  }

  run()
}
