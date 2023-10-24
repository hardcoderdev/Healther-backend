package hardcoder.dev.healtherbackend.routing.advices

import io.ktor.resources.*

@Resource("/advices")
class Advices {

    @Resource("/images")
    class Images(val advices: Advices = Advices()) {
        @Resource("/{name}")
        class ImageFileName(val images: Images = Images(), val name: String) {}
    }

    @Resource("/{id}")
    class Id(val advices: Advices = Advices(), val id: Int) {

        @Resource("/pages")
        class Pages(val adviceId: Id) {
            @Resource("/delete_all_pages")
            class DeleteAllPages(val parent: Pages)

            @Resource("/{pageId}")
            class PageId(val parent: Pages, val pageId: Int) {

                @Resource("/delete")
                class DeletePage(val parent: PageId, val pageId: Int)

                @Resource("/update")
                class UpdatePage(val parent: PageId, val pageId: Int)
            }
        }

        @Resource("/delete")
        class DeleteAdvice(val adviceId: Id)

        @Resource("/update")
        class UpdateAdvice(val adviceId: Id)
    }
}