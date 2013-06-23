/*
__________       .__                     .___
\______   \ ____ |  | _____    ____    __| _/
 |       _//  _ \|  | \__  \  /    \  / __ |
 |    |   (  <_> )  |__/ __ \|   |  \/ /_/ |
 |____|_  /\____/|____(____  /___|  /\____ |
        \/                 \/     \/      \/
Copyright (c), 2013, roland@tritsch.org
http://www.tritsch.org
*/

package org.tritsch.scala.josephus

import org.apache.commons.collections.iterators.LoopingListIterator
import java.util.{List => JList, LinkedList => JLinkedList}
import java.lang.{Integer => JInteger}

import scala.annotation.tailrec

import com.typesafe.scalalogging.slf4j.Logging

/** Find a solution to Josephus Problem.
  *
  * @author roland@tritsch.org
  * @todo add more logging
  */
object Josephus {
  /** Returns the soldier that will be last (surviving).
    *
    * @param numOfSoldiers number of soldiers in the ring.
    * @param skipping number of soldiers to skip, before killing the next one.
    */
  final def findSurvivor(numOfSoldiers: Int, skipping: Int): Int = {
    require(numOfSoldiers >= 1)
    require(skipping >= 0)

    val soldiers: JList[JInteger] = new JLinkedList[JInteger]()
    for(i <- 1 to numOfSoldiers) soldiers.add(new JInteger(i))
    val ringOfSoldiers = new LoopingListIterator(soldiers)

    ringOfSoldiers.reset
    while(ringOfSoldiers.size > 1) {
      for(i <- 0 to skipping) ringOfSoldiers.next
      ringOfSoldiers.remove
    }

    assert(ringOfSoldiers.size == 1)
    ringOfSoldiers.next.asInstanceOf[JInteger]
  }

  /** Same as findSurvivor, but using a tail recursion. */
  final def findSurvivorRecursively(numOfSoldiers: Int, skipping: Int): Int = {
    require(numOfSoldiers >= 1)
    require(skipping >= 0)

    val soldiers: JList[JInteger] = new JLinkedList[JInteger]()
    for(i <- 1 to numOfSoldiers) soldiers.add(new JInteger(i))
    val ringOfSoldiers = new LoopingListIterator(soldiers)

    @tailrec
    def findSurvivorRecursion: Int = {
      require(ringOfSoldiers.size >= 1)

      if(ringOfSoldiers.size == 1) ringOfSoldiers.next.asInstanceOf[JInteger]
      else {
        for(i <- 0 to skipping) ringOfSoldiers.next
        ringOfSoldiers.remove
        findSurvivorRecursion
      }
    }
    findSurvivorRecursion
  }

  /** Main method: Do the arg line processing and call the functions. */
  final def main(args: Array[String]): Unit = {
    require(args.size == 2, "Usage: Josephus <numOfSoldiers> <killingEvery>")

    val numOfSoldiers = args(0).toInt
    val killingEvery = args(1).toInt

    println(findSurvivor(numOfSoldiers, killingEvery-1))
    println(findSurvivorRecursively(numOfSoldiers, killingEvery-1))
    System.exit(0)
  }
}
