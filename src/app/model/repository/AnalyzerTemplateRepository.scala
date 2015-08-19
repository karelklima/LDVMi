package model.repository

import model.entity._
import CustomUnicornPlay.driver.simple._

import scala.slick.lifted.TableQuery

class AnalyzerTemplateRepository extends CrudRepository[AnalyzerTemplateId, AnalyzerTemplate, AnalyzerTemplateTable](TableQuery[AnalyzerTemplateTable]) {

  def findAllWithMandatoryDescriptors(implicit session: Session): Seq[AnalyzerTemplate] = {
    (for {
      sct <- query
      ctf <- componentFeaturesQuery if ctf.componentTemplateId === sct.componentTemplateId
      f <- featuresQuery if f.isMandatory && f.id === ctf.featureId
      d <- descriptorsQuery if d.featureId === f.id
    } yield sct).list.distinct
  }

  def findByComponentIds(ids: Seq[ComponentTemplateId])(implicit session: Session): Seq[SpecificComponentTemplate] = {
    (for {
      s <- query if s.componentTemplateId inSetBind ids
    } yield s).list
  }
}
