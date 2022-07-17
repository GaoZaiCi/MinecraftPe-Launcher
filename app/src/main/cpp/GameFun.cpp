#include "GameFun.h"

GameFun *mFun;

shared_ptr<MinecraftGame> *MinecraftGame::instance;


GameFun::GameFun(void *base) : base((uintptr_t) base) {
    mFun = this;
    MinecraftGame::instance = ptr<shared_ptr<MinecraftGame> *>(0x000000000A1098E0);//Java_com_mojang_minecraftpe_MainActivity_nativeShutdown
}

GameFun &GameFun::get() {
    return *mFun;
}

ClientInstance *MinecraftGame::getPrimaryClientInstance() const {
    return GameFun::callV<83, ClientInstance *>(this);
}

SceneFactory *MinecraftGame::getPrimarySceneFactory() const {
    return GameFun::callV<155, SceneFactory *>(this);
}

LocalPlayer *MinecraftGame::getPrimaryLocalPlayer() const {
    return GameFun::callV<517, LocalPlayer *>(this);
}

GuiData *MinecraftGame::getPrimaryGuiData() const {
    return GameFun::callV<93, GuiData *>(this);
}

std::unordered_map<u_char, std::shared_ptr<IClientInstance>> &MinecraftGame::getClientInstanceMap() const {
    return GameFun::callV<84, std::unordered_map<u_char, std::shared_ptr<IClientInstance>> &>(this);
}

FontHandle *MinecraftGame::getFontHandle() const {
    return GameFun::callV<198, FontHandle *>(this);
}

Level *MinecraftGame::getLocalServerLevel() const {
    return GameFun::callV<412, Level *>(this);
}

ServerInstance *MinecraftGame::getServerInstance() const {
    return GameFun::callV<137, ServerInstance *>(this);
}

CameraManager *ClientInstance::getCameraManager() {
    return GameFun::callV<179, CameraManager *>(this);
}

SceneStack *ClientInstance::getClientSceneStack() const {
    return GameFun::callV<218, SceneStack *>(this);
}

GuiData *ClientInstance::getGuiData() const {
    return GameFun::callV<207, GuiData *>(this);
}

GameRenderer *ClientInstance::getGameRenderer() {
    return GameFun::callV<175, GameRenderer *>(this);
}

LevelRenderer *ClientInstance::getLevelRenderer() const {
    return GameFun::callV<177, LevelRenderer *>(this);
}

Options *ClientInstance::getOptions() const {
    return GameFun::callV<169, Options *>(this);
}

Minecraft *ClientInstance::getServerData() const {
    return GameFun::callV<163, Minecraft *>(this);
}

Level *ClientInstance::getLevel() const {
    return GameFun::callV<164, Level *>(this);
}

ItemInHandRenderer *ClientInstance::getItemInHandRenderer() {
    return GameFun::callV<316, ItemInHandRenderer *>(this);
}

ItemRenderer *ClientInstance::getItemRenderer() {
    return GameFun::callV<317, ItemRenderer *>(this);
}

double ClientInstance::getServerConnectionTime() {
    return GameFun::callV<317, double>(this);
}

void ClientInstance::setServerPingTime(int value) {
    GameFun::callV<320>(this, value);
}

int ClientInstance::getServerPingTime() {
    return GameFun::callV<321, int>(this);
}

SoundEngine *ClientInstance::getSoundEngine() const {
    return GameFun::callV<283, SoundEngine *>(this);
}

LoopbackPacketSender *ClientInstance::getPacketSender() {
    return GameFun::callV<262, LoopbackPacketSender *>(this);
}

NetworkHandler *ClientInstance::getClientNetworkSystem() const {
    return GameFun::callV<263, NetworkHandler *>(this);
}

unique_ptr<FontHandle> ClientInstance::getFontHandle() const {
    return GameFun::callV<88, unique_ptr<FontHandle>>(this);
}

unique_ptr<FontHandle> ClientInstance::getRuneFontHandle() const {
    return GameFun::callV<89, unique_ptr<FontHandle>>(this);
}

unique_ptr<FontHandle> ClientInstance::getUnicodeFontHandle() const {
    return GameFun::callV<90, unique_ptr<FontHandle>>(this);
}

void GameMode::attack(Actor &actor) {
    GameFun::callV<15>(this, &actor);
}

bool GameMode::buildBlock(BlockPos const &pos, u_char side) {
    return GameFun::callV<7, bool>(this, &pos, side);
}

bool GameMode::useItem(ItemStack &item) {
    return GameFun::callV<12, bool>(this, &item);
}

bool GameMode::useItemOn(ItemStack &item, BlockPos const &pos, u_char side, Vec3 const &pos1, Block const *block) {
    return GameFun::callV<13, bool>(this, &pos, side, &pos1, block);
}

bool GameMode::destroyBlock(const BlockPos &pos, u_char side, bool arg) {
    return GameFun::callV<3, bool>(this, &pos, side, arg);
}

bool GameMode::interact(Actor &actor, const Vec3 &pos) {
    return GameFun::callV<14, bool>(this, &actor, &pos);
}

void LevelData::getTagData(const CompoundTag &tag) {
    mFun->call(McOffset::_ZN9LevelData10getTagDataERK11CompoundTag, this, &tag);
}

void LevelData::setTagData(CompoundTag &tag) {
    mFun->call(McOffset::_ZNK9LevelData10setTagDataER11CompoundTag, this, &tag);
}

void Tag::write(IDataOutput &data) const {
    GameFun::callV<3>(this, &data);
}

void Tag::load(IDataInput &data) {
    GameFun::callV<4>(this, &data);
}

void Tag::writeScriptData(IDataOutput &data) const {
    GameFun::callV<5>(this, &data);
}

void Tag::loadScriptData(IDataInput &data) {
    GameFun::callV<6>(this, &data);
}

std::string Tag::toString() const {
    return GameFun::callV<7, std::string>(this);
}

Tag::Type Tag::getId() const {
    return GameFun::callV<6, Tag::Type>(this);
}

bool Tag::equals(const Tag &tag) const {
    return GameFun::callV<9, bool>(this, &tag);
}

std::unique_ptr<Tag> Tag::copy() const {
    return GameFun::callV<12, std::unique_ptr<Tag>>(this);
}

uint64_t Tag::hash() const {
    return GameFun::callV<13, uint64_t>(this);
}

std::unique_ptr<Tag> Tag::newTag(Tag::Type type) {
    return mFun->call<std::unique_ptr<Tag>>(McOffset::_ZN3Tag6newTagENS_4TypeE, type);
}

ByteArrayTag::~ByteArrayTag() {
    GameFun::callV<1>(this);
}

ByteTag::~ByteTag() {
    GameFun::callV<1>(this);
}

CompoundTag::~CompoundTag() {
    GameFun::callV<1>(this);
}

Tag *CompoundTag::get(std::string const &name) const {
    return mFun->call<Tag *>(McOffset::_ZNK11CompoundTag3getERKNSt6__ndk112basic_stringIcNS0_11char_traitsIcEENS0_9allocatorIcEEEE, this, &name);
}

void CompoundTag::put(std::string name, std::unique_ptr<Tag> tag) {
    mFun->ptr < void(*)
    (CompoundTag * , std::string, std::unique_ptr<Tag>) > (McOffset::_ZN11CompoundTag3putENSt6__ndk112basic_stringIcNS0_11char_traitsIcEENS0_9allocatorIcEEEENS0_10unique_ptrI3TagNS0_14default_deleteIS8_EEEE)(this, std::move(name), move(tag));
}

bool CompoundTag::remove(std::string const &name) {
    return mFun->call<bool>(McOffset::_ZN11CompoundTag6removeERKNSt6__ndk112basic_stringIcNS0_11char_traitsIcEENS0_9allocatorIcEEEE, this, &name);
}

DoubleTag::~DoubleTag() {
    GameFun::callV<1>(this);
}

FloatTag::~FloatTag() {
    GameFun::callV<1>(this);
}

Int64Tag::~Int64Tag() {
    GameFun::callV<1>(this);
}

IntArrayTag::~IntArrayTag() {
    GameFun::callV<1>(this);
}

IntTag::~IntTag() {
    GameFun::callV<1>(this);
}

ListTag::~ListTag() {
    GameFun::callV<1>(this);
}

ShortTag::~ShortTag() {
    GameFun::callV<1>(this);
}

StringTag::~StringTag() {
    GameFun::callV<1>(this);
}


void Level::addParticle(ParticleType type, Vec3 const &start_pos, Vec3 const &end_pos, int size, CompoundTag const *tag, bool arg) {
    GameFun::callV<204>(this, type, &start_pos, &end_pos, size, tag, &arg);
}

Actor *Level::fetchEntity(ActorUniqueID actorUniqueId, bool arg) const {
    return GameFun::callV<48, Actor *>(this, actorUniqueId, arg);
}

HitResult *Level::getHitResult() {
    return GameFun::callV<382, HitResult *>(this);
}

HitResult *Level::getLiquidHitResult() {
    return GameFun::callV<383, HitResult *>(this);
}

LoopbackPacketSender *Level::getPacketSender() const {
    return GameFun::callV<377, LoopbackPacketSender *>(this);
}

int Level::getSeed() {
    return GameFun::callV<112, int>(this);
}

void Level::setTime(int time) {
    GameFun::callV<111>(this, time);
}

void Level::requestMapInfo(ActorUniqueID uniqueId, bool arg) {
    GameFun::callV<283>(this, &uniqueId, arg);
}

MapItemSavedData *Level::createMapSavedData(const ActorUniqueID &uniqueId, const BlockPos &pos, AutomaticID<Dimension, int> *id, int scale) {
    return GameFun::callV<284, MapItemSavedData *>(this, &uniqueId, &pos, id, scale);
}

MapItemSavedData *Level::getMapSavedData(const CompoundTag &tag) {
    return GameFun::callV<281, MapItemSavedData *>(this, &tag);
}

MapItemSavedData *Level::getMapSavedData(const CompoundTag *tag) {
    return GameFun::callV<282, MapItemSavedData *>(this, tag);
}

const std::unordered_map<mce::UUID, PlayerListEntry> &Level::getPlayerList() const {
    return *GameFun::callV<364, std::unordered_map<mce::UUID, PlayerListEntry> *>(this);
}

LevelData *Level::getLevelData() const {
    return GameFun::callV<146, LevelData *>(this);
}

bool Level::isClientSide() const {
    return GameFun::callV<363, bool>(this);
}

void Actor::addEffect(MobEffectInstance const &instance) {
    //mFun->call(McOffset::_ZN5Actor9addEffectERK17MobEffectInstance, this, &instance);
}

bool Actor::canAddRider(Actor &actor) const {
    return GameFun::callV<190, bool>(this, &actor);
}

void Actor::doWaterSplashEffect() {
    GameFun::callV<269>(this);
}

ActorDefinitionIdentifier *Actor::getActorIdentifier() const {
    //return mFun->call<ActorDefinitionIdentifier *>(McOffset::_ZNK5Actor18getActorIdentifierEv, this);
}

ItemStack *Actor::getArmor(ArmorSlot slot) const {
    return GameFun::callV<152, ItemStack *>(this, slot);
}

AttributeInstance &Actor::getAttribute(Attribute const &attribute) const {
    return GameFun::callV<204, AttributeInstance &>(this, &attribute);
}

ItemStack *Actor::getCarriedItem() const {
    return GameFun::callV<162, ItemStack *>(this);
}

EntityType Actor::getEntityTypeId() const {
    return GameFun::callV<171, EntityType>(this);
}

Level *Actor::getLevel() const {
    return mFun->call<Level *>(0x0000000004452B24, this);
}

ItemStack *Actor::getOffhandSlot() const {
    //return mFun->call<ItemStack *>(McOffset::_ZNK5Actor14getOffhandSlotEv, this);
}

ItemStack *Actor::getEquippedTotem() const {
    return GameFun::callV<161, ItemStack *>(this);
}

void Actor::consumeTotem() {
    GameFun::callV<162>(this);
}

Vec3 &Actor::getPos() const {
    return GameFun::callV<18, Vec3 &>(this);
}

BlockSource *Actor::getRegion() const {
    return mFun->call<BlockSource *>(0x000000000445669C, this);
}

ActorRuntimeID Actor::getRuntimeID() const {
    return mFun->call<ActorRuntimeID>(0x00000000044588E4, this);
}

ActorUniqueID &Actor::getUniqueID() const {
    return mFun->call<ActorUniqueID &>(0x0000000004458958, this);
}

bool Actor::hasEnteredWater() const {
    return GameFun::callV<71, bool>(this);
}

bool Actor::isInLava() const {
    return GameFun::callV<74, bool>(this);
}

bool Actor::isInWater() const {
    return GameFun::callV<70, bool>(this);
}

bool Actor::isRemoved() const {
    //return mFun->call<bool>(McOffset::_ZNK5Actor9isRemovedEv, this);
}

bool Actor::isSneaking() const {
    //return mFun->call<bool>(McOffset::_ZNK5Actor10isSneakingEv, this);
}

void Actor::lerpTo(Vec3 const &pos, Vec2 const &rot, int arg) {
    GameFun::callV<42>(this, &pos, &rot, arg);
}

void Actor::teleportTo(const Vec3 &pos, bool arg, int arg1, int arg2) {
    GameFun::callV<39>(this, &pos, arg, arg1, arg2);
}

void Actor::lerpMotion(const Vec3 &pos) {
    GameFun::callV<43>(this, &pos);
}

bool Actor::operator==(Actor &actor) const {
    return mFun->call<bool>(0x000000000445FC3C, this, &actor);
}

void Actor::remove() {
    GameFun::callV<14>(this);
}

void Actor::setPos(Vec3 const &pos) {
    GameFun::callV<15>(this, &pos);
}

void Actor::setSize(float x, float y) {
    GameFun::callV<232>(this, x, y);
}

void Actor::setStatusFlag(ActorFlags flag, bool value) {
    //mFun->call(McOffset::_ZN5Actor13setStatusFlagE10ActorFlagsb, this, flag, value);
}

bool Actor::getStatusFlag(ActorFlags flag) const {
    //return mFun->call<bool>(McOffset::_ZNK5Actor13getStatusFlagE10ActorFlags, this, flag);
}

void Actor::setNameTag(const string &name) {
    GameFun::callV<66>(this, &name);
}

Actor *Actor::getRide() const {
    //return mFun->call<Actor *>(McOffset::_ZNK5Actor7getRideEv, this);
}

void Actor::addPredictionMoveData(const MoveActorAbsoluteData &data) {
    //mFun->call(McOffset::_ZN5Actor21addPredictionMoveDataERK21MoveActorAbsoluteData, this, &data);
}

SpatialNetworkData *Actor::getSpatialNetworkData() const {
    //return mFun->call<SpatialNetworkData *>(McOffset::_ZNK5Actor21getSpatialNetworkDataEv, this);
}

void Actor::save(CompoundTag &tag) {
    GameFun::callV<163>(this, &tag);
}

void Actor::load(const CompoundTag &tag, DataLoadHelper &helper) {
    GameFun::callV<165>(this, &tag, &helper);
}

void Actor::load(const CompoundTag &tag) {
    //DefaultDataLoadHelper *ptr = GameFun::get().ptr<DefaultDataLoadHelper*>(McOffset::_ZTV21DefaultDataLoadHelper);
    //load(tag,*(DefaultDataLoadHelper*)&ptr);
}

void Actor::readAdditionalSaveData(const CompoundTag &tag, DataLoadHelper &helper) {
    GameFun::callV<261>(this, &tag, &helper);
}

void Actor::readAdditionalSaveData(const CompoundTag &tag) {
    //DefaultDataLoadHelper *ptr = GameFun::get().ptr<DefaultDataLoadHelper*>(McOffset::_ZTV21DefaultDataLoadHelper);
    //readAdditionalSaveData(tag,*(DefaultDataLoadHelper*)&ptr);
}

void Actor::addAdditionalSaveData(CompoundTag &tag) {
    GameFun::callV<262>(this, &tag);
}

void Actor::startSwimming() {
    GameFun::callV<199>(this);
}

bool Actor::isJumping() const {
    return GameFun::callV<127, bool>(this);
}

MerchantRecipeList *Actor::getTradeOffers() const {
    //return mFun->call<MerchantRecipeList *>(McOffset::_ZN5Actor14getTradeOffersEv, this);
}

float Actor::getYHeadRot() const {
    return GameFun::callV<223, float>(this);
}

bool Actor::onClimbableBlock() const {
    //return mFun->call<bool>(McOffset::_ZNK5Actor16onClimbableBlockEv, this);
}

void Actor::swing() {
    GameFun::callV<216>(this);
}

bool Actor::isAlive() const {
    return GameFun::callV<100, bool>(this);
}

void Actor::setLeashHolder(ActorUniqueID id) {
    //mFun->call(McOffset::_ZN5Actor14setLeashHolderE13ActorUniqueID,this,id);
}

ActorUniqueID Actor::getLeashHolder() const {
    //return mFun->call<ActorUniqueID>(McOffset::_ZNK5Actor14getLeashHolderEv,this);
}

void Actor::setTarget(Actor *actor) {
    GameFun::callV<110>(this, actor);
}

Dimension *Actor::getDimension() const {
    return mFun->call<Dimension *>(0x000000000445AE74, this);
}

void LocalPlayer::displayClientMessage(const string &message) {
    GameFun::callV<389>(this, &message);
}

void LocalPlayer::sendNetworkPacket(Packet &packet) const {
    GameFun::callV<432>(this, &packet);
}

std::unique_ptr<CompoundTag> ItemStackBase::save() const {
    return mFun->call<std::unique_ptr<CompoundTag>>(McOffset::_ZNK13ItemStackBase4saveEv, this);
}

ItemStackBase::~ItemStackBase() {
    mFun->call(0x0000000003F44B90, this);
}

void ItemStackBase::_setItem(int id, bool arg) {
    mFun->call(0x00000000050299DC, this, id, arg);
}

void ItemStack::fromTag(const CompoundTag &nbt) {
    mFun->call(McOffset::_ZN9ItemStack7fromTagERK11CompoundTag, this, &nbt);
}

ItemStack::ItemStack() {
    mFun->call(0x0000000005024814, this);
}

ItemStack::ItemStack(const ItemStack &itemStack) {
    mFun->call(0x0000000005025004, this, &itemStack);
}

InventoryAction::InventoryAction(const InventorySource &source, uint slot, const ItemStack &sourceItem, const ItemStack &targetItem) {
    mFun->call(0x00000000043652C4, this, &source, slot, &sourceItem, &targetItem);
}

InventoryAction::~InventoryAction() {
    mFun->call(0x00000000043650C4, this);
}

void InventoryTransactionManager::addAction(const InventoryAction &action, bool arg) {
    mFun->call(0x0000000005455350, this, &action, arg);
}

std::unordered_map<ActorUniqueID, gsl::not_null<Actor *>> &Dimension::getEntityIdMap() const {
    return mFun->call<std::unordered_map<ActorUniqueID, gsl::not_null<Actor *>> &>(0x000000000429DF48, this);
}

void Dimension::forEachPlayer(std::function<bool(Player &)> fun) {
    mFun->call(0x000000000429DF68, this, &fun);
}

ActorEventPacket::ActorEventPacket(ActorRuntimeID runtimeId, ActorEvent event, int value) {
    mFun->call(0x0000000005BE27A0, this, runtimeId, event, value);
}

ActorEventPacket::~ActorEventPacket() {
    GameFun::callV<1>(this);
}

MinecraftPacketIds Packet::getId() const {
    return GameFun::callV<2, MinecraftPacketIds>(this);
}

std::string Packet::getName() const {
    return GameFun::callV<3, std::string>(this);
}
